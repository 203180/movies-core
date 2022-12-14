package com.example.webmailcore.controllers;

import com.example.webmailcore.exceptions.BadRequestError;
import com.example.webmailcore.models.AirplaneCompany;
import com.example.webmailcore.services.AirplaneCompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
        return service.all();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createOrganization(@RequestBody AirplaneCompany airplaneCompany) throws BadRequestError {
        return ResponseEntity.ok(service.create(airplaneCompany));
    }
}
