package com.example.webmailcore.controllers;

import com.example.webmailcore.exceptions.BadRequestError;
import com.example.webmailcore.models.AirplaneCompany;
import com.example.webmailcore.models.DestinationPairDTO;
import com.example.webmailcore.models.User;
import com.example.webmailcore.services.AirplaneCompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/airplaneCompanies")
public class AirplaneCompanyController {

    @Autowired
    AirplaneCompanyService service;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllAirplaneCompanies(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestParam(value = "orderDirection", required = false) String orderDirection,
            @RequestParam(value = "searchParams", required = false) String searchParams
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap filterMap = objectMapper.readValue(searchParams, HashMap.class);
        return ResponseEntity.ok(service.all(filterMap, PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(orderDirection), orderBy))));
    }

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public List<AirplaneCompany> getAllAirplaneCompaniesWithoutPaging() {
        return service.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createOrganization(@RequestBody AirplaneCompany airplaneCompany) throws BadRequestError {
        return ResponseEntity.ok(service.create(airplaneCompany));
    }

    @RequestMapping(path = "/getAllUsersPerAirline/{airlineId}", method = RequestMethod.GET)
    public List<User> getAllUsersPerAirline(@PathVariable String airlineId) {
        return service.getAllUsersPerAirline(airlineId);
    }

    @GetMapping("/airline/{airlineId}/destinations")
    public ResponseEntity<Page<DestinationPairDTO>> getDistinctDestinationPairs(
            @PathVariable("airlineId") String airlineId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<DestinationPairDTO> result = service.getAllDistinctDestinationPairs(airlineId, pageable);

        return ResponseEntity.ok(result);
    }

}
