package com.example.webmailcore.repositories;

import com.example.webmailcore.enums.MailboxMailType;
import com.example.webmailcore.models.Mailbox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailboxRepository extends JpaRepository<Mailbox, String> {
    List<Mailbox> findAll();
    Page<Mailbox> findAll(Specification<Mailbox> specification, Pageable pageable);
    List<Mailbox> findAllByReadIsFalse();
    Integer countAllByReadIsFalseAndMailType(MailboxMailType mailboxMailType);
    Mailbox findByMailId(String mailboxId);
}