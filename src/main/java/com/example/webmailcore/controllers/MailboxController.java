package com.example.webmailcore.controllers;

import com.example.webmailcore.models.Mailbox;
import com.example.webmailcore.models.MailboxAttachment;
import com.example.webmailcore.models.ReplyMailDto;
import com.example.webmailcore.repositories.MailboxRepository;
import com.example.webmailcore.services.MailboxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping("/mailbox")
public class MailboxController {

    @Autowired
    MailboxService mailboxService;

    @Autowired
    MailboxRepository mailboxRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity checkMailbox(
    ) {
        return mailboxService.checkMailBox();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/allMail")
    public Page<Mailbox> getAllMail(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size
    ) {
        return mailboxService.getAllMail(PageRequest.of(page, size));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable("id") String id) throws SQLException {
        return ResponseEntity.ok(mailboxService.getById(id));
    }

    @RequestMapping(path = "/attachments/{attachmentID}", method = RequestMethod.GET)
    public ResponseEntity downloadAttachments(
            @PathVariable(value = "attachmentID") String attachmentID
    ) throws IOException {
        Optional<MailboxAttachment> attachment = mailboxService.getMailboxAttachment(attachmentID);
        File file = new File(attachment.get().getPath());
        if (!file.exists()) throw new FileNotFoundException();
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + file.getName() + "\"");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @RequestMapping(path = "/getAll", method = RequestMethod.GET)
    public ResponseEntity getAll(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "orderBy") String orderBy,
            @RequestParam(value = "orderDirection") String orderDirection,
            @RequestParam(value = "searchParams") String searchParams
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap filterMap = objectMapper.readValue(searchParams, HashMap.class);
        Sort sort;
        if (orderBy != null && orderDirection != null) {
            sort = Sort.by(orderDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, orderBy);
        } else {
            sort = Sort.by(Sort.Direction.DESC, "dateSent");
        }
        return ResponseEntity.ok(mailboxService.all(filterMap, PageRequest.of(page, size, sort)));
    }

    @RequestMapping(value = "/{id}/read", method = RequestMethod.PUT)
    public ResponseEntity markAsRead(@PathVariable String id) {
        return ResponseEntity.ok(mailboxService.markMailAsRead(id));
    }

    @RequestMapping(value = "/{id}/unread", method = RequestMethod.PUT)
    public ResponseEntity markAsUnread(@PathVariable String id) {
        return ResponseEntity.ok(mailboxService.markMailAsUnread(id));
    }

    @RequestMapping(value = "/{id}/archive", method = RequestMethod.PUT)
    public ResponseEntity archiveMail(@PathVariable String id) {
        return ResponseEntity.ok(mailboxService.archiveMail(id));
    }

    @RequestMapping(value = "/{id}/unarchive", method = RequestMethod.PUT)
    public ResponseEntity unarchiveMail(@PathVariable String id) {
        return ResponseEntity.ok(mailboxService.unarchiveMail(id));
    }

    @RequestMapping(value = "/{id}/markMailHasTicket", method = RequestMethod.PUT)
    public ResponseEntity markMailHasTicket(@PathVariable String id) {
        return ResponseEntity.ok(mailboxService.markMailHasTicket(id));
    }

    @RequestMapping(value = "/allUnreadMail", method = RequestMethod.GET)
    public ResponseEntity getAllUnreadMail() {
        return ResponseEntity.ok(mailboxService.getAllUnreadMailNumber());
    }

    @RequestMapping(value = "/sendReplyMail", method = RequestMethod.POST)
    public ResponseEntity sendReplyMail(
            @RequestBody ReplyMailDto replyMailDto
    ) {
        ResponseEntity response = mailboxService.sendReplyMail(replyMailDto.getReplyToID(), replyMailDto.getContent());
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/sendReplyMailAndChangeStatus", method = RequestMethod.POST)
    public ResponseEntity sendReplyMailAndChangeStatus(
            @RequestBody ReplyMailDto replyMailDto
    ) {
        ResponseEntity response = mailboxService.sendReplyMail(replyMailDto.getReplyToID(), replyMailDto.getContent());
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(response);
    }
}
