package com.example.webmailcore.controllers;

import com.example.webmailcore.exceptions.BadRequestError;
import com.example.webmailcore.models.AirplaneCompany;
import com.example.webmailcore.models.DestinationRegion;
import com.example.webmailcore.models.LoyaltyCard;
import com.example.webmailcore.services.DestinationRegionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/destinationRegions")
public class DestinationRegionController {

    @Autowired
    DestinationRegionService destinationRegionService;

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
        return ResponseEntity.ok(destinationRegionService.all(filterMap, PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(orderDirection), orderBy))));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all")
    public ResponseEntity getAllWithoutPaging(){
        return ResponseEntity.ok(destinationRegionService.getAll());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(destinationRegionService.getById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody DestinationRegion destinationRegion) throws BadRequestError {
        return ResponseEntity.ok(destinationRegionService.create(destinationRegion));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody DestinationRegion destinationRegion) {
        return ResponseEntity.ok(destinationRegionService.update(destinationRegion));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(destinationRegionService.delete(id));
    }
}
