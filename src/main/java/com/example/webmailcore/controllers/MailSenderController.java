package com.example.webmailcore.controllers;

import com.example.webmailcore.models.User;
import com.example.webmailcore.services.mail.MailSenderService;
import com.itextpdf.text.DocumentException;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;


@RestController
@RequestMapping(path = "/mailSender")
public class MailSenderController {

    @Autowired
    MailSenderService mailSenderService;

    @RequestMapping(value = "/sendMailForLoyaltyCardBenefits", method = RequestMethod.POST)
    public ResponseEntity sendMailForLoyaltyCardBenefits(@RequestBody User user) throws EmailException {
        ResponseEntity response = mailSenderService.sendMailForLoyaltyCardBenefits(user);
        return ResponseEntity.ok(response);

    }

    @RequestMapping(value = "/sendPromoMailToFilteredUsers", method = RequestMethod.POST)
    public ResponseEntity sendPromoMailToFilteredUsers(@RequestBody List<User> users) throws EmailException {
        ResponseEntity response = mailSenderService.sendPromoMailToFilteredUsers(users);
        return ResponseEntity.ok(response);

    }
}

