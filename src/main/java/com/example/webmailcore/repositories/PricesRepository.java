package com.example.webmailcore.repositories;

import com.example.webmailcore.models.Prices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PricesRepository extends JpaRepository<Prices, String>, JpaSpecificationExecutor<Prices> {

}
