package com.example.webmailcore.repositories;

import com.example.webmailcore.models.LoyaltyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyCardRepository extends JpaRepository<LoyaltyCard, String>, JpaSpecificationExecutor<LoyaltyCard> {

    LoyaltyCard findByName(String name);
}