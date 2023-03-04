package com.example.webmailcore.controllers;

import com.example.webmailcore.models.Country;
import com.example.webmailcore.models.Prices;
import com.example.webmailcore.services.PricesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/prices")
public class PricesController {

    @Autowired
    PricesService service;

    @RequestMapping(path = "/all",method = RequestMethod.GET)
    public ResponseEntity getAllPricesWithoutPaging() throws IOException {
        return ResponseEntity.ok(service.getAll());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getPrice(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
