package com.example.webmailcore.services.mail;

import com.example.webmailcore.enums.MailboxMailType;
import com.example.webmailcore.models.Mailbox;
import com.example.webmailcore.repositories.MailboxRepository;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;


@Service
public class MailSenderService {

    //Mandrill API key: md-IwJMkQoqJBqpW4o0eZxl-Q

    @Autowired
    MailboxRepository mailboxRepository;

    public ResponseEntity sendMail(String subject, String content, String to) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("astraairairlines@gmail.com", "sglvmosysbshkjot"));
        email.setSSLOnConnect(true);
        email.setFrom("astraairairlines@gmail.com");
        InternetAddress internetAddress = new InternetAddress();
        internetAddress.setAddress(to);
        List<InternetAddress> internetAddressList = new ArrayList<>();
        internetAddressList.add(internetAddress);
        email.setTo(internetAddressList);
        email.setSubject(subject);
        email.setMsg(content);
        email.setCharset("UTF-8");
        email.send();
        Mailbox mailbox = new Mailbox();
        mailbox.setMailType(MailboxMailType.OUTGOING);
        mailboxRepository.save(mailbox);

        return ResponseEntity.ok("Mail sent");
    }
}



