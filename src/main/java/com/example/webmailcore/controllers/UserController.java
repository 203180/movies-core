package com.example.webmailcore.controllers;

import com.example.webmailcore.auth.CustomUserDetails;
import com.example.webmailcore.exceptions.BadRequestError;
import com.example.webmailcore.models.User;
import com.example.webmailcore.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

    @Secured({"ROLE_ADMINISTRATION", "ROLE_CLIENT_ADMIN"})
    @RequestMapping(method = RequestMethod.GET, path = "/groupMembers")
    public ResponseEntity getAllGroupMembers(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "groupId") String groupId,
            @RequestParam(value = "username", required = false) String username
    ) throws IOException {
        if (username == null) {
            username = "";
        }
        return ResponseEntity.ok(userService.findAllByGroupId(groupId, username, PageRequest.of(page, size)));
    }

    @Secured({"ROLE_ADMINISTRATION", "ROLE_CLIENT_ADMIN"})
    @RequestMapping(method = RequestMethod.GET, path = "/groupNotMembers")
    public ResponseEntity getAllGroupNotMembers(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "groupId") String groupId,
            @RequestParam(value = "username", required = false) String username
    ) throws IOException {
        if (username == null) {
            username = "";
        }
        return ResponseEntity.ok(userService.findAllByGroupIdNot(groupId, username, PageRequest.of(page, size)));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getUser(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @RequestMapping(path = "/me/details", method = RequestMethod.GET)
    public ResponseEntity getCurrentUserDetails() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
    @Secured({"ROLE_ADMINISTRATION", "ROLE_CLIENT_ADMIN"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody User user) throws BadRequestError {
        return ResponseEntity.ok(userService.save(user));
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @Secured({"ROLE_ADMINISTRATION", "ROLE_CLIENT_ADMIN"})
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateUser(@RequestBody User user) throws BadRequestError {
        return ResponseEntity.ok(userService.save(user));
    }

    @Secured({"ROLE_ADMINISTRATION","ROLE_ASTA_ADRIA_AGENT", "ROLE_CLIENT_ADMIN", "ROLE_CLIENT"})
    @RequestMapping(path = "/reset_password", method = RequestMethod.PUT)
    public ResponseEntity resetPassword(
            @RequestParam String username,
            @RequestParam String newPassword,
            @RequestParam String oldPassword
    ) {
        if (username.equalsIgnoreCase(userService.getCurrentUser().getUsername())) {
            return ResponseEntity.ok(userService.resetPassword(username, newPassword, oldPassword));
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Secured({"ROLE_ADMINISTRATION", "ROLE_CLIENT_ADMIN"})
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable(value = "id") String id) throws BadRequestError {
        return ResponseEntity.ok(userService.remove(id));
    }

    @RequestMapping(path = "/my", method = RequestMethod.PUT)
    public ResponseEntity updateMyProfile(@RequestBody User user) {
        if (user.getUsername().equalsIgnoreCase(userService.getCurrentUser().getUsername())) {
            return ResponseEntity.ok(userService.save(user));
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
