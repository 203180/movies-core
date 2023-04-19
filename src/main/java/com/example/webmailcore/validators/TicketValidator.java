package com.example.webmailcore.validators;


import com.example.webmailcore.exceptions.BadRequestError;
import com.example.webmailcore.models.FlightTicket;
import org.springframework.stereotype.Component;

@Component
public class TicketValidator {


    public void validateTicketCompleteness(FlightTicket ticket) throws BadRequestError {

        if(ticket.getUser() == null) {
            throw new BadRequestError("Cannot create ticket without passenger info");
        }

        if(ticket.getCityFrom() == null) {
            throw new BadRequestError("Cannot create ticket without departure city");
        }

        if(ticket.getCityTo() == null) {
            throw new BadRequestError("Cannot create ticket without arrival city");
        }

        if(ticket.getDepartureDate() == null) {
            throw new BadRequestError("Cannot create ticket without departure date");
        }

        if(ticket.getBoardingHour() == null) {
            throw new BadRequestError("Cannot create ticket without boarding hour");
        }

        if(ticket.getFlightNumber() == null) {
            throw new BadRequestError("Cannot create ticket without flight number");
        }

        if(ticket.getGate() == null) {
            throw new BadRequestError("Cannot create ticket without gate");
        }

        if(ticket.getSeat() == null) {
            throw new BadRequestError("Cannot create ticket without seat number");
        }
    }
}
