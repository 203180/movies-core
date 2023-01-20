package com.example.webmailcore.services.mail;

import com.example.webmailcore.enums.MailMessageStatus;
import com.example.webmailcore.enums.MailTemplateType;
import com.example.webmailcore.models.mail.MailMessage;
import com.example.webmailcore.models.mail.MailTemplate;
import com.example.webmailcore.repositories.mail.MailMessageRepository;
import com.example.webmailcore.repositories.mail.MailTemplateRepository;
import com.example.webmailcore.services.AbstractService;
import com.example.webmailcore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import javax.mail.Session;
import javax.mail.Store;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Service
public class MailMessageService implements AbstractService<MailMessage, String> {

    @Autowired
    MailMessageRepository mailMessageRepository;

    @Autowired
    MailTemplateRepository mailTemplateRepository;

    @Autowired
    UserService userService;

    @Override
    public Page<MailMessage> all(Pageable pageable) {
        return mailMessageRepository.findAll(pageable);
    }

    @Override
    public Boolean remove(String id) {
        mailMessageRepository.deleteById(id);
        return Boolean.TRUE;
    }

    @Override
    public MailMessage get(String id) {
        return mailMessageRepository.getById(id);
    }

    @Override
    public MailMessage save(MailMessage mailMessage) {
        if (mailMessage.getId() == null) {
            mailMessage.setStatus(MailMessageStatus.PENDING);
            mailMessage.setRequeued(0);
            mailMessage.setCreatedBy(userService.getCurrentUser());
        } else {
            mailMessage.setModifiedBy(userService.getCurrentUser());
        }
        return mailMessageRepository.save(mailMessage);
    }

    @Override
    public MailMessage save(MailMessage mailMessage, UserDetails creator) {
        if (mailMessage.getId() == null) {
            mailMessage.setStatus(MailMessageStatus.PENDING);
            mailMessage.setRequeued(0);
            mailMessage.setCreatedBy(userService.getCurrentUser());
        } else {
            mailMessage.setModifiedBy(userService.getCurrentUser());
        }
        return mailMessageRepository.save(mailMessage);
    }

    @Override
    public Boolean remove(String id, UserDetails creator) {
        return null;
    }

    public Page<MailMessage> getAllByStatus(Pageable pageable, String status) {
        return mailMessageRepository.findAllByStatus(pageable, MailMessageStatus.valueOf(status));
    }

    public MailMessage save(MailMessage mailMessage, MailTemplateType mailTemplateType, HashMap<String, String> params) {
        MailTemplate mailTemplate = mailTemplateRepository.findFirstByTemplateType(mailTemplateType);
        mailMessage = prepareMailWithTemplate(mailMessage, mailTemplateType, params);
        return save(mailMessage);
    }

    public MailMessage prepareMailWithTemplate(MailMessage mailMessage, MailTemplateType mailTemplateType, HashMap<String, String> params) {
        MailTemplate mailTemplate = mailTemplateRepository.findFirstByTemplateType(mailTemplateType);
        String mailContent = mailTemplate.getContent();
        List<String> templateParams = mailTemplate.getMailTemplateParameters();
        if (params != null) {
            for (int i = 0; i < templateParams.size(); i++) {
                if(params.get(templateParams.get(i)) != null) {
                    mailContent = mailContent.replace("{" + templateParams.get(i) + "}", params.get(templateParams.get(i)));
                }else{
                    mailContent = mailContent.replace("{" + templateParams.get(i) + "}", "");
                }
            }
            mailMessage.setSubject(params.get("subject"));
        }
        mailMessage.setContent("<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "</head>" + mailContent);
        return mailMessage;
    }

    public MailMessage requeue(String id) {
        MailMessage mailMessage = mailMessageRepository.getById(id);
        if (mailMessage != null) {
            mailMessage.setStatus(MailMessageStatus.PENDING);
            if (mailMessage.getRequeued() == null) {
                mailMessage.setRequeued(0);
            }
            mailMessage.setRequeued(mailMessage.getRequeued() + 1);
            mailMessage = mailMessageRepository.save(mailMessage);
        }
        return mailMessage;
    }

    public MailMessage cancelMailMessage(String id) {
        MailMessage mailMessage = mailMessageRepository.getById(id);
        if (mailMessage != null) {
            mailMessage.setStatus(MailMessageStatus.CANCELED);
            mailMessage = mailMessageRepository.save(mailMessage);
        }
        return mailMessage;
    }

    public ResponseEntity testImapConnection(String imapHost, String imapPort, String userName, String password) throws Exception {
        Properties props = System.getProperties();
        try {
            if (imapPort.equalsIgnoreCase("993")) {
                props.setProperty("mail.store.protocol", "imaps");
                props.setProperty("mail.imaps.port", "993");
                props.setProperty("mail.imaps.ssl.trust", "*");
                Session session = Session.getDefaultInstance(props, null);
                //Create the POP3 store object and connect to the pop store.
                Store store = session.getStore("imaps");
                store.connect(imapHost, 993, userName, password);
                store.close();
            } else {
                props.setProperty("mail.store.protocol", "imap");
                props.setProperty("mail.imap.port", "143");
                props.setProperty("mail.imap.ssl.trust", "*");
                Session session = Session.getDefaultInstance(props, null);
                //Create the POP3 store object and connect to the pop store.
                Store store = session.getStore("imap");
                store.connect(imapHost, 143, userName, password);
                store.close();
            }
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
