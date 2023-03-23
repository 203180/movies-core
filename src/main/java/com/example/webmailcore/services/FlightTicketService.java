package com.example.webmailcore.services;

import com.example.webmailcore.enums.AncillaryType;
import com.example.webmailcore.enums.TicketStatus;
import com.example.webmailcore.models.Client;
import com.example.webmailcore.models.FlightTicket;
import com.example.webmailcore.repositories.FlightTicketRepository;
import com.example.webmailcore.repositories.specifications.ClientSpecification;
import com.example.webmailcore.repositories.specifications.FlightTicketSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlightTicketService {

    @Autowired
    FlightTicketRepository repository;

    public List<FlightTicket> getAll() {
        return repository.findAll();
    }

    public Page<FlightTicket> all(Map<String, String> params, Pageable pageable) {
        FlightTicketSpecification specification = new FlightTicketSpecification();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        return repository.findAll(specification, pageable);
    }

    public List<FlightTicket> getAllTicketsByUser(String userId) {
        List<FlightTicket> tickets = repository.findAllByUser_Id(userId);
        return tickets;
    }

    public Page<FlightTicket> getAllTicketsByUser(String userId, Pageable pageable) {
        Page<FlightTicket> tickets = repository.findAllByUser_Id(userId, pageable);
        return tickets;
    }

    public FlightTicket save(FlightTicket flightTicket) {
        return repository.save(flightTicket);
    }

    public FlightTicket getById(String id) {
        FlightTicket flightTicket = repository.getById(id);
        return flightTicket;
    }

    public FlightTicket create(FlightTicket flightTicket) {
        flightTicket.setMailSentForCanceledTicket(false);
        flightTicket.setMailSentForDelayedTicket(false);
        return repository.save(flightTicket);
    }

    public FlightTicket update(FlightTicket flightTicket) {
        return repository.save(flightTicket);
    }

    public Boolean delete(String id) {
        FlightTicket flightTicket = repository.getById(id);
        repository.delete(flightTicket);
        return true;
    }

    public List<TicketStatus> getAllTicketStatuses() {
        List<TicketStatus> ticketStatuses = new ArrayList<>();
        for (TicketStatus ticketStatus : TicketStatus.values()) {
            ticketStatuses.add(ticketStatus);
        }
        return ticketStatuses;
    }

    public List<AncillaryType> getAncillaryTypes() {
        List<AncillaryType> ancillaryTypes = new ArrayList<>();
        for (AncillaryType ancillaryType : AncillaryType.values()) {
            ancillaryTypes.add(ancillaryType);
        }
        return ancillaryTypes;
    }


    public FlightTicket setTicketStatusToArrived(String ticketId) {
        FlightTicket ticket = getById(ticketId);
        ticket.setStatus(TicketStatus.ARRIVED);
        ticket = repository.save(ticket);
        return ticket;
    }

    public FlightTicket setTicketStatusToCanceled(String ticketId) {
        FlightTicket ticket = getById(ticketId);
        ticket.setStatus(TicketStatus.CANCELED);
        ticket = repository.save(ticket);
        return ticket;
    }

    public FlightTicket setTicketStatusToDelayed (String ticketId) {
        FlightTicket ticket = getById(ticketId);
        ticket.setStatus(TicketStatus.DELAYED);
        ticket = repository.save(ticket);
        return ticket;
    }

}
