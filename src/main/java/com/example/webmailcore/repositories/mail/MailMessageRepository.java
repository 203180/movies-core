package com.example.webmailcore.repositories.mail;

import com.example.webmailcore.enums.MailMessageStatus;
import com.example.webmailcore.models.mail.MailMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MailMessageRepository extends JpaRepository<MailMessage, String> {
    Page<MailMessage> findAllByStatus(Pageable pageable, MailMessageStatus status);
}

