package com.example.webmailcore.controllers;

import com.example.webmailcore.auth.CustomUserDetails;
import com.example.webmailcore.models.User;
import com.example.webmailcore.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllUsers(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "orderBy") String orderBy,
            @RequestParam(value = "orderDirection") String orderDirection,
            @RequestParam(value = "searchParams") String searchParams
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap filterMap = objectMapper.readValue(searchParams, HashMap.class);
        User user = userService.getCurrentUser();
        CustomUserDetails customUserDetails = userService.getCurrentUserDetails();
        if (!customUserDetails.getAuthorities().stream().anyMatch(r -> r.getAuthority().startsWith("ROLE_ADMINISTRATION"))) {
            filterMap.put("airplaneCompany.id", user.getAirplaneCompany().getId());
        }
        return ResponseEntity.ok(userService.all(filterMap, PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(orderDirection), orderBy))));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/airplaneCompanyMembers")
    public ResponseEntity getAllAirplaneCompanyMembers(
            @RequestParam(value = "airplaneCompanyId", required = false) String airplaneCompanyId
    ) throws IOException {
        return ResponseEntity.ok(userService.allAirplaneCompanyMembers(airplaneCompanyId));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getUser(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @RequestMapping(path = "/me/details", method = RequestMethod.GET)
    public ResponseEntity getCurrentUserDetails() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
}
