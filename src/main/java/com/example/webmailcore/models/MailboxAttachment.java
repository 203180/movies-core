package com.example.webmailcore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "MAILBOX_ATTACHMENT")
public class MailboxAttachment extends AbstractEntity {

    @Column(name = "FILE_NAME")
    String fileName;

    @Column(name = "FILE_PATH")
    String path;

    @Column(name = "FILE_EXTENSION")
    String fileExtension;

    @Column(name = "SIZE")
    Long size;

    @Column(name = "MIME_TYPE")
    String mimeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAILBOX_ID")
    @JsonIgnore
    Mailbox mail;

}