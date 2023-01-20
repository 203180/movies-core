package com.example.webmailcore.models.mail;

import com.example.webmailcore.enums.MailTemplateType;
import com.example.webmailcore.models.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "MAIL_TEMPLATES")
public class MailTemplate extends AbstractEntity implements Serializable {

    @Column(name = "TEMPLATE_TYPE")
    @Enumerated(EnumType.STRING)
    MailTemplateType templateType;

    @Column(name = "NAME")
    String name;

    @Column(name = "CONTENT", length = 2048)
    String content;

    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    List<String> mailTemplateParameters;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;
}
