package com.example.webmailcore.repositories;

import com.example.webmailcore.models.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, String>, JpaSpecificationExecutor<Privilege> {
    Privilege findByName(String name);
}

