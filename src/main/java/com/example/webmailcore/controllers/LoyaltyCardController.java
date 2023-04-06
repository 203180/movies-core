package com.example.webmailcore.controllers;

import com.example.webmailcore.models.LoyaltyCard;
import com.example.webmailcore.services.LoyaltyCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/loyaltyCards")
public class LoyaltyCardController {

    @Autowired
    LoyaltyCardService loyaltyCardService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllLoyaltyCards(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "orderBy") String orderBy,
            @RequestParam(value = "orderDirection") String orderDirection,
            @RequestParam(value = "searchParams") String searchParams
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap filterMap = objectMapper.readValue(searchParams, HashMap.class);
        return ResponseEntity.ok(loyaltyCardService.all(filterMap, PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(orderDirection), orderBy))));
    }

    //    @Secured({"ROLE_ADMINISTRATION", "ROLE_ASTA_ADRIA_AGENT"})
    @RequestMapping(path = "/all",method = RequestMethod.GET)
    public ResponseEntity getAllLoyaltyCardsWithoutPaging() throws IOException {
        return ResponseEntity.ok(loyaltyCardService.getAll());
    }


    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getLoyaltyCard(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(loyaltyCardService.getById(id));
    }

    //    @Secured({"ROLE_ADMINISTRATION"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody LoyaltyCard loyaltyCard) {
        return ResponseEntity.ok(loyaltyCardService.create(loyaltyCard));
    }

    //    @Secured({"ROLE_ADMINISTRATION"})
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody LoyaltyCard loyaltyCard) {
        return ResponseEntity.ok(loyaltyCardService.update(loyaltyCard));
    }

    //    @Secured({"ROLE_ADMINISTRATION"})
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(loyaltyCardService.delete(id));
    }

}
