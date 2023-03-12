package com.example.webmailcore.services;

import com.example.webmailcore.enums.MailboxMailType;
import com.example.webmailcore.models.Mailbox;
import com.example.webmailcore.models.MailboxAttachment;
import com.example.webmailcore.repositories.MailboxAttachmentRepository;
import com.example.webmailcore.repositories.MailboxRepository;
import com.example.webmailcore.repositories.specifications.MailboxSpecification;
import com.example.webmailcore.repositories.specifications.SearchCriteria;
import com.example.webmailcore.repositories.specifications.SearchOperation;
import com.sun.mail.imap.IMAPMessage;
import org.apache.poi.util.IOUtils;
import org.hibernate.Hibernate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import com.google.common.io.Files;
import org.springframework.util.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.security.MessageDigest;
import java.util.*;

@Service
public class MailboxService {

    @Autowired
    MailboxRepository mailboxRepository;

    @Autowired
    MailboxAttachmentRepository mailboxAttachmentRepository;

    @Value("${saveDirectory}")
    private String SAVE_DIRECTORY;

    @Value("${mailboxUsername}")
    private String username;

    @Value("${mailboxPassword}")
    private String password;

    @Value("${mailboxEmailSMTPServer}")
    private String emailSMTPserver;

    @Value("${mailboxSMTPPort}")
    private String emailSMTPPort;

    @Value("${mailboxMailStoreType}")
    private String mailStoreType;

    public Mailbox save(Mailbox mailbox) {
        return mailboxRepository.save(mailbox);
    }

    public Page<Mailbox> all(Map<String, String> params, Pageable pageable) {
        MailboxSpecification mailboxSpecification = new MailboxSpecification();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (!StringUtils.isEmpty(entry.getKey()) && !StringUtils.isEmpty(entry.getValue())) {
                if (entry.getKey().equals("mailType")) {
                    mailboxSpecification.add(new SearchCriteria("mailType", MailboxMailType.valueOf(entry.getValue()), SearchOperation.EQUAL));
                } else if (entry.getKey().equals("read")) {
                    Boolean value;
                    if (entry.getValue().equals("true"))
                        value = Boolean.TRUE;
                    else if (entry.getValue().equals("false"))
                        value = Boolean.FALSE;
                    else
                        value = null;

                    if (value != null)
                        mailboxSpecification.add(new SearchCriteria(entry.getKey(), value, SearchOperation.EQUAL));

                } else if (entry.getKey().equals("hasTickets")) {
                    Boolean value;
                    if (entry.getValue().equals("true"))
                        value = Boolean.TRUE;
                    else if (entry.getValue().equals("false"))
                        value = Boolean.FALSE;
                    else
                        value = null;

                    if (value != null)
                        mailboxSpecification.add(new SearchCriteria(entry.getKey(), value, SearchOperation.EQUAL));

                } else if (entry.getKey().equals("archived")) {
                    mailboxSpecification.add(new SearchCriteria(entry.getKey(), entry.getValue(), SearchOperation.EQUAL));
                } else
                    mailboxSpecification.add(new SearchCriteria(entry.getKey(), entry.getValue(), SearchOperation.MATCH));
            }
        }
        Page<Mailbox> results = mailboxRepository.findAll(mailboxSpecification, pageable);
        return results;
    }

    @Scheduled(fixedRate = 60000)
    public ResponseEntity checkMailBox() {
        try {

            Properties properties = new Properties();

            String host = "imap.gmail.com";
            String user = "astraairairlines@gmail.com";
            String password = "sglvmosysbshkjot";


            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            Store store = emailSession.getStore("imaps");

            store.connect(host, user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);

            if (emailFolder.exists()) {
                Message[] messages = emailFolder.search(unseenFlagTerm);

                for (int i = 0; i < messages.length; i++) {
                    Message message = messages[i];
                    MessageDigest md = MessageDigest.getInstance("MD5");

                    md.update(message.getFrom()[0].toString().getBytes(StandardCharsets.UTF_8));
                    String id = message.getSentDate().toInstant().toEpochMilli() + DigestUtils.md5DigestAsHex(message.getFrom()[0].toString().getBytes());
                    if (!mailboxRepository.existsById(id)) {
                        Mailbox mail = new Mailbox();
                        mail.setId(id);
                        mail.setSender(message.getFrom()[0].toString());
                        mail.setSubject(message.getSubject());
                        mail.setDateSent(message.getSentDate());
                        mail.setRead(false);
                        mail.setArchived(false);
                        mail.setHasTickets(false);
                        mail.setMessageNumber(message.getMessageNumber());

//                        StringBuilder messageContent = new StringBuilder();
                        StringBuilder messageMailId = new StringBuilder();
                        StringBuilder messageReplyMailId = new StringBuilder();

                        List<MailboxAttachment> attachments = new ArrayList<>();
                        if (message.getContentType().contains("multipart")) {
                            attachments = downloadAttachments(message, id);
                        }
                        mail.setAttachments(attachments);
                        writePart(message, messageMailId, messageReplyMailId);
                        mail.setMailId(messageMailId.toString());

                        mail.setMailType(MailboxMailType.INCOMING);
                        Document doc = Jsoup.parse(getContentFromMessage(message));
                        String textOnly = doc.body().text(); // "An example link"
                        mail.setContentTextOnly(textOnly);

                        String finalContent = getContentFromMessage(message);
                        Map<String, String> inlineImages = checkForInlineImages(message);
                        for (Map.Entry<String, String> entry : inlineImages.entrySet()) {
                            finalContent = finalContent.replace("cid:" + entry.getKey(), "data:image/png;base64," + entry.getValue());
                        }
                        mail.setContent(finalContent);
                        mail = mailboxRepository.save(mail);
                        if (!messageReplyMailId.toString().isEmpty()) {
                            Mailbox replyMail = mailboxRepository.findByMailId(messageReplyMailId.toString());
                        }
                    }
                    message.setFlag(Flags.Flag.SEEN, true);
                }
            }

            emailFolder.close();
            store.close();

            return ResponseEntity.ok(true);


        } catch (NoSuchProviderException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (MessagingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    private Map<String, String> checkForInlineImages(Message message) throws MessagingException, IOException {
        Map<String, String> inlineImages = new HashMap<>();
        if (!message.getContent().getClass().equals(String.class)) {
            Multipart multipart = (Multipart) message.getContent();
            int numberOfParts = multipart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(partCount);
                if (Part.INLINE.equalsIgnoreCase(part.getDisposition())) {
                    inlineImages.put(part.getContentID().replace("<", "").replace(">", ""), new String(Base64.getEncoder().encode(IOUtils.toByteArray((InputStream) part.getContent())), "UTF-8"));
                } else if (part.getContentType().startsWith("IMAGE/")) {
                    inlineImages.put(part.getContentID().replace("<", "").replace(">", ""), new String(Base64.getEncoder().encode(IOUtils.toByteArray((InputStream) part.getContent())), "UTF-8"));
                } else if (part.getContentType().startsWith("multipart/RELATED;")) {
                    Multipart relatedParts = (Multipart) part.getContent();
                    int numberOfRelatedParts = relatedParts.getCount();
                    for (int relatedPartCount = 0; relatedPartCount < numberOfRelatedParts; relatedPartCount++) {
                        MimeBodyPart relatedPart = (MimeBodyPart) relatedParts.getBodyPart(relatedPartCount);
                        if (Part.INLINE.equalsIgnoreCase(relatedPart.getDisposition())) {
                            inlineImages.put(relatedPart.getContentID().replace("<", "").replace(">", ""), new String(Base64.getEncoder().encode(IOUtils.toByteArray((InputStream) relatedPart.getContent())), "UTF-8"));
                        } else if (relatedPart.getContentType().startsWith("IMAGE/")) {
                            inlineImages.put(relatedPart.getContentID().replace("<", "").replace(">", ""), new String(Base64.getEncoder().encode(IOUtils.toByteArray((InputStream) relatedPart.getContent())), "UTF-8"));
                        }
                    }
                }
            }
        }
        return inlineImages;
    }

    private List<MailboxAttachment> downloadAttachments(Message message, String id) throws IOException, MessagingException {
        List<MailboxAttachment> downloadedAttachments = new ArrayList<>();
        Multipart multipart = (Multipart) message.getContent();
        int numberOfParts = multipart.getCount();
        for (int partCount = 0; partCount < numberOfParts; partCount++) {
            MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(partCount);
            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                MailboxAttachment attachment = new MailboxAttachment();
                String path = id;
                File idFile = new File(SAVE_DIRECTORY + File.separator + path);
                if (idFile.exists()) {
                    String fileName = part.getFileName().replace("/","_");
                    attachment.setFileName(fileName);
                    part.saveFile(idFile.getAbsolutePath() + File.separator + fileName);
                    attachment.setPath(idFile.getAbsolutePath() + File.separator + fileName);
                    attachment.setMimeType(Files.getFileExtension(fileName));
                    attachment.setFileExtension(Files.getFileExtension(fileName));
                    attachment.setSize((long) part.getSize());
                    attachment.setMail(mailboxRepository.getById(id));
                    downloadedAttachments.add(attachment);
                } else {
                    idFile.mkdir();
                    String fileName =  part.getFileName().replace("/","_");
                    attachment.setFileName(fileName);
                    part.saveFile(idFile.getAbsolutePath() + File.separator + fileName);
                    attachment.setPath(idFile.getAbsolutePath() + File.separator + fileName);
                    attachment.setMimeType(Files.getFileExtension(fileName));
                    attachment.setFileExtension(Files.getFileExtension(fileName));
                    attachment.setSize((long) part.getSize());
                    attachment.setMail(mailboxRepository.getById(id));
                    downloadedAttachments.add(attachment);
                }
            }
        }
        return downloadedAttachments;
    }

    public Page<Mailbox> getAllMail(PageRequest of) {
        Page<Mailbox> page = mailboxRepository.findAll(PageRequest.of(of.getPageNumber(), of.getPageSize(), Sort.by(Sort.Direction.DESC, "dateSent")));
        return page;
    }

    public Optional<MailboxAttachment> getMailboxAttachment(String attachmentId) {
        return mailboxAttachmentRepository.findById(attachmentId);
    }

    public Mailbox getById(String id) {
        Mailbox mailbox = mailboxRepository.findById(id).orElse(null);
        mailbox.setNewContent(mailbox.getContent());
        return mailbox;
    }


    public static void writePart(Part p, StringBuilder messageMailId, StringBuilder messageReplyMailId) throws Exception {
        if (p instanceof Message) {
            messageMailId.append(((IMAPMessage) p).getMessageID());
            if (((IMAPMessage) p).getInReplyTo() != null)
                messageReplyMailId.append(((IMAPMessage) p).getInReplyTo());
        }
    }

    public Mailbox markMailAsRead(String id) {
        Mailbox mailbox = mailboxRepository.findById(id).get();
        mailbox.setRead(true);
        return mailboxRepository.save(mailbox);
    }

    public Mailbox markMailAsUnread(String id) {
        Mailbox mailbox = mailboxRepository.findById(id).get();
        mailbox.setRead(false);
        return mailboxRepository.save(mailbox);
    }

    public Mailbox archiveMail(String id) {
        Mailbox mailbox = mailboxRepository.findById(id).get();
        mailbox.setArchived(true);
        return mailboxRepository.save(mailbox);
    }

    public Mailbox unarchiveMail(String id) {
        Mailbox mailbox = mailboxRepository.findById(id).get();
        mailbox.setArchived(false);
        return mailboxRepository.save(mailbox);
    }


    public Object markMailHasTicket(String id) {
        Mailbox mailbox = mailboxRepository.findById(id).get();
        mailbox.setHasTickets(true);
        return mailboxRepository.save(mailbox);
    }


    public Integer getAllUnreadMailNumber() {
        return mailboxRepository.countAllByReadIsFalseAndMailType(MailboxMailType.INCOMING);
    }


    @Transactional
    public ResponseEntity sendReplyMail(String replyToID, String content) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", emailSMTPserver);
            props.put("mail.smtp.port", emailSMTPPort);
            props.put("mail.smtp.ssl.enable", "true");
            props.setProperty("mail.smtp.auth", "true");
            props.put("mail.smtp.debug", "true");
            props.put("mail.smtp.socketFactory.port", emailSMTPPort);
            props.put("mail.smtp.socketFactory.fallback", "false");

            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);

            Store mailStore = session.getStore(mailStoreType);
            mailStore.connect("imap.gmail.com", username, password);

            Folder folder = mailStore.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            Mailbox mail = mailboxRepository.getById(replyToID);
            Hibernate.initialize(mail);
            int emailNo = mail.getMessageNumber();

            Message emailMessage = folder.getMessage((emailNo));
            Message mimeMessage;
            mimeMessage = emailMessage.reply(false);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.setContent(content, "text/html");
            mimeMessage.addRecipient(Message.RecipientType.TO, emailMessage.getFrom()[0]);
            mimeMessage.setFlag(Flags.Flag.SEEN, false);

            Transport.send(mimeMessage);

            String id = mimeMessage.getSentDate().toInstant().toEpochMilli() + DigestUtils.md5DigestAsHex(mimeMessage.getFrom()[0].toString().getBytes());
            if (!mailboxRepository.existsById(id)) {
                Mailbox mailTmp = new Mailbox();
                mailTmp.setId(id);
                mailTmp.setSender(mimeMessage.getFrom()[0].toString());
                mailTmp.setReceiver(emailMessage.getFrom()[0].toString());
                mailTmp.setSubject(mimeMessage.getSubject());
                mailTmp.setDateSent(mimeMessage.getSentDate());
                mailTmp.setRead(false);
                mailTmp.setArchived(false);
                mailTmp.setMessageNumber(mimeMessage.getMessageNumber());

                List<MailboxAttachment> attachments = new ArrayList<>();
                if (mimeMessage.getContentType().contains("multipart")) {
                    attachments = downloadAttachments(mimeMessage, id);
                }
                mailTmp.setAttachments(attachments);
                mailTmp.setMailId(((MimeMessage) mimeMessage).getMessageID());
                mailTmp.setMailType(MailboxMailType.OUTGOING);
                Document doc = Jsoup.parse(mimeMessage.getContent().toString());
                String textOnly = doc.body().text(); // "An example link"
                mailTmp.setContentTextOnly(textOnly);
                mailTmp.setContent(mimeMessage.getContent().toString());
                mailTmp = mailboxRepository.save(mailTmp);

            }

            folder.close();
            mailStore.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            System.err.println("Error in replying email.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error in replying email.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(true);
    }

    private class SMTPAuthenticator extends
            javax.mail.Authenticator {
        public PasswordAuthentication
        getPasswordAuthentication() {
            return new PasswordAuthentication(username,
                    password);
        }
    }

    private boolean textIsHtml = false;

    private String getContentFromMessage(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getContentFromMessage(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getContentFromMessage(bp);
                    if (s != null)
                        return s;
                } else {
                    return getContentFromMessage(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getContentFromMessage(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }
        return null;
    }

}
