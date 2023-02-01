package com.example.webmailcore.controllers;

import com.example.webmailcore.models.City;
import com.example.webmailcore.services.CityService;
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
@RequestMapping("/cities")
public class CityController {

    @Autowired
    CityService cityService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllCities(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "orderBy") String orderBy,
            @RequestParam(value = "orderDirection") String orderDirection,
            @RequestParam(value = "searchParams") String searchParams
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap filterMap = objectMapper.readValue(searchParams, HashMap.class);
        return ResponseEntity.ok(cityService.all(filterMap, PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(orderDirection), orderBy))));
    }

//    @Secured({"ROLE_ADMINISTRATION", "ROLE_ASTA_ADRIA_AGENT"})
    @RequestMapping(path = "/all",method = RequestMethod.GET)
    public ResponseEntity getAllCitiesWithoutPaging() throws IOException {
        return ResponseEntity.ok(cityService.getAll());
    }


    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getCity(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(cityService.getById(id));
    }

//    @Secured({"ROLE_ADMINISTRATION"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createCity(@RequestBody City city) {
        return ResponseEntity.ok(cityService.create(city));
    }

//    @Secured({"ROLE_ADMINISTRATION"})
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateCity(@RequestBody City city) {
        return ResponseEntity.ok(cityService.update(city));
    }

//    @Secured({"ROLE_ADMINISTRATION"})
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCity(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(cityService.delete(id));
    }

}
