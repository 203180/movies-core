package com.example.webmailcore.repositories;

import com.example.webmailcore.models.AirplaneCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneCompanyRepository extends JpaRepository<AirplaneCompany, String>, JpaSpecificationExecutor<AirplaneCompany> {

    AirplaneCompany findByName(String name);

}
