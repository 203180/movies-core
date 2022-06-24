package com.example.webmailcore.models;

import com.example.webmailcore.enums.MailboxMailType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "MAILBOX")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Mailbox extends AbstractEntity {

    @Column(name = "SENDER")
    private String sender;

    @Column(name= "RECEIVER")
    private String receiver;

    @Column(name = "SUBJECT")
    private String subject;

    @Lob
    @JsonIgnore
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CONTENT_TEXT_ONLY", length = 1000000)
    private String contentTextOnly;

    @Column(name = "DATE_SENT")
    private Date dateSent;

    @Transient
    private String newContent;

    @OneToMany(mappedBy = "mail", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<MailboxAttachment> attachments;;

    @Column(name = "READ")
    private Boolean read;

    @Column(name = "HAS_TICKETS")
    private Boolean hasTickets;

    @Column(name= "ARCHIVED")
    private Boolean archived;

    @Column(name = "MESSAGE_NUMBER")
    private Integer messageNumber;

    @Column(name = "MAIL_ID")
    private String mailId;

    @Column(name = "MAIL_TYPE")
    @Enumerated(EnumType.STRING)
    private MailboxMailType mailType;

}
