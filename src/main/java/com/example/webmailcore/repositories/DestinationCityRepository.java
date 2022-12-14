package com.example.webmailcore.repositories;

import com.example.webmailcore.models.DestinationCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationCityRepository extends JpaRepository<DestinationCity, String>, JpaSpecificationExecutor<DestinationCity> {

}

