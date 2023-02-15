package com.example.webmailcore.controllers;

import com.example.webmailcore.models.Privilege;
import com.example.webmailcore.services.PrivilegeService;
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
@RequestMapping(path = "/privileges")
public class PrivilegeController {

    @Autowired
    private PrivilegeService privilegeService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllPrivileges(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "orderBy") String orderBy,
            @RequestParam(value = "orderDirection") String orderDirection,
            @RequestParam(value = "searchParams") String searchParams
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap filterMap = objectMapper.readValue(searchParams, HashMap.class);
        return ResponseEntity.ok(privilegeService.all(filterMap, PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(orderDirection), orderBy))));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getPrivilege(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(privilegeService.get(id));
    }

//    @Secured({"ROLE_ADMINISTRATION"})
    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public ResponseEntity getAllPrivilegesWithoutPaging() {
        return ResponseEntity.ok(privilegeService.all());
    }

//    @Secured({"ROLE_ADMINISTRATION"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createPrivilege(@RequestBody Privilege privilege) {
        return ResponseEntity.ok(privilegeService.save(privilege));
    }

//    @Secured({"ROLE_ADMINISTRATION"})
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updatePrivilege(@RequestBody Privilege privilege)  {
        return ResponseEntity.ok(privilegeService.save(privilege));
    }

//    @Secured({"ROLE_ADMINISTRATION"})
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deletePrivilege(@PathVariable(value = "id") String id)  {
        return ResponseEntity.ok(privilegeService.remove(id));
    }
}
