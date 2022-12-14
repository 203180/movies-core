package com.example.webmailcore.controllers;

import com.example.webmailcore.services.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/countries")
public class CountryController {

    @Autowired
    CountryService countryService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllCountries(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "orderBy") String orderBy,
            @RequestParam(value = "orderDirection") String orderDirection,
            @RequestParam(value = "searchParams") String searchParams
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap filterMap = objectMapper.readValue(searchParams, HashMap.class);
        return ResponseEntity.ok(countryService.all(filterMap, PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(orderDirection), orderBy))));
    }

    @RequestMapping(path = "/all",method = RequestMethod.GET)
    public ResponseEntity getAllCountriesWithoutPaging() throws IOException {
        return ResponseEntity.ok(countryService.getAll());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getCountry(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(countryService.getById(id));
    }

}
