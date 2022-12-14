package com.example.webmailcore.repositories;

import com.example.webmailcore.models.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, String>, JpaSpecificationExecutor<Destination> {
}
