package com.example.webmailcore.repositories;

import com.example.webmailcore.models.DestinationRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationRegionRepository extends JpaRepository<DestinationRegion, String>, JpaSpecificationExecutor<DestinationRegion> {
}
