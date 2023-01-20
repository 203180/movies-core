package com.example.webmailcore.repositories.mail;

import com.example.webmailcore.enums.MailTemplateType;
import com.example.webmailcore.models.mail.MailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface MailTemplateRepository extends JpaRepository<MailTemplate, String> {
    List<MailTemplate> findAll();
    MailTemplate findFirstByTemplateType(MailTemplateType templateType);
    MailTemplate findByTemplateType(MailTemplateType templateType);
}

