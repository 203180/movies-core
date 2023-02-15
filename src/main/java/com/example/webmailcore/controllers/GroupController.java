package com.example.webmailcore.controllers;

import com.example.webmailcore.models.Group;
import com.example.webmailcore.services.GroupService;
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
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllGroups(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "orderBy") String orderBy,
            @RequestParam(value = "orderDirection") String orderDirection,
            @RequestParam(value = "searchParams") String searchParams
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap filterMap = objectMapper.readValue(searchParams, HashMap.class);
        return ResponseEntity.ok(groupService.all(filterMap, PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(orderDirection), orderBy))));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getGroup(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(groupService.getById(id));
    }

//    @Secured({"ROLE_ADMINISTRATION", "ROLE_CLIENT_ADMIN"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupService.create(group));
    }

//    @Secured({"ROLE_ADMINISTRATION", "ROLE_CLIENT_ADMIN"})
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupService.update(group));
    }

//    @Secured({"ROLE_ADMINISTRATION", "ROLE_CLIENT_ADMIN"})
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteGroup(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(groupService.delete(id));
    }
}
