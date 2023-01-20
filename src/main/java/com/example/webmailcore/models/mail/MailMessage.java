package com.example.webmailcore.models.mail;

import com.example.webmailcore.enums.MailMessageStatus;
import com.example.webmailcore.models.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "MAIL_MESSAGE")
public class MailMessage extends AbstractEntity implements Serializable {

    @Column(name = "SENDER")
    private String sender;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="MAIL_MESSAGE_RECEIVERS")
    @Fetch(FetchMode.SUBSELECT)
    List<String> receivers;


    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "CONTENT", length = 2048)
    private String content;

    @Column(name = "REQUEUED")
    private Integer requeued;

    @Column(name = "DATE_SENT")
    private Date dateSent;

    @Column(name ="FAILURE_REASON")
    private String failureReason;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private MailMessageStatus status;
}