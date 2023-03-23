package com.example.webmailcore.repositories;

import com.example.webmailcore.models.FlightTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightTicketRepository extends JpaRepository<FlightTicket, String>, JpaSpecificationExecutor<FlightTicket> {

    List<FlightTicket> findAllByUser_Id(String userId);
    List<FlightTicket> findAllByAirplaneCompany_Id(String airlineId);
    Page<FlightTicket> findAllByUser_Id(String userId, Pageable pageable);

}
