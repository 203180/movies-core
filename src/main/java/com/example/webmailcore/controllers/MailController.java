package com.example.webmailcore.controllers;

import com.example.webmailcore.models.mail.MailMessage;
import com.example.webmailcore.services.mail.MailMessageService;
import com.example.webmailcore.services.mail.MailSenderService;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


@RestController
public class MailController {

    @Autowired
    MailSenderService mailSenderService;

    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    public ResponseEntity sendMail(@RequestBody MailMessage mailMessage) throws EmailException {
        ResponseEntity response = mailSenderService.sendMail("Test mail", "Hello,world!", "anchovasimona11@gmail.com");
        return ResponseEntity.ok(response);
    }
}

