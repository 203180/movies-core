package com.example.webmailcore.repositories;

import com.example.webmailcore.models.MailboxAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MailboxAttachmentRepository extends JpaRepository<MailboxAttachment, String>, JpaSpecificationExecutor<MailboxAttachment> {

}

