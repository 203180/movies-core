package com.example.webmailcore.controllers;

import com.example.webmailcore.models.FlightTicket;
import com.example.webmailcore.services.FlightTicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/ticket")
public class FlightTicketController {

    @Autowired
    FlightTicketService service;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAll(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "orderBy") String orderBy,
            @RequestParam(value = "orderDirection") String orderDirection,
            @RequestParam(value = "searchParams") String searchParams
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap filterMap = objectMapper.readValue(searchParams, HashMap.class);
        return ResponseEntity.ok(service.all(filterMap, PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(orderDirection), orderBy))));
    }

    @RequestMapping(path = "/all",method = RequestMethod.GET)
    public ResponseEntity getAllWithoutPaging() throws IOException {
        return ResponseEntity.ok(service.getAll());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody FlightTicket flightTicket) {
        return ResponseEntity.ok(service.create(flightTicket));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody FlightTicket flightTicket) {
        return ResponseEntity.ok(service.update(flightTicket));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @RequestMapping(path = "/allByUser/{username}",method = RequestMethod.GET)
    public ResponseEntity getAllTicketsByUser(@PathVariable(value = "username") String userId) throws IOException {
        return ResponseEntity.ok(service.getAllTicketsByUser(userId));
    }

}
