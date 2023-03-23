package com.example.webmailcore.controllers;

import com.example.webmailcore.models.mail.MailMessage;
import com.example.webmailcore.services.mail.MailMessageService;
import com.example.webmailcore.services.mail.MailSenderService;
import com.itextpdf.text.DocumentException;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;


@RestController
public class MailController {

    @Autowired
    MailSenderService mailSenderService;

//    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
//    public ResponseEntity sendMail() throws EmailException {
//        ResponseEntity response = mailSenderService.sendMail();
//        return ResponseEntity.ok(response);
//    }


    @RequestMapping(value = "/sendMailToBookedTickets", method = RequestMethod.POST)
    public ResponseEntity sendMailToBookedTickets() throws EmailException, DocumentException, FileNotFoundException {
        ResponseEntity response = mailSenderService.sendMailToBookedTickets();
        return ResponseEntity.ok(response);
    }
}

