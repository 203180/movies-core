package com.example.webmailcore.services;

import com.example.webmailcore.models.Privilege;
import com.example.webmailcore.repositories.PrivilegeRepository;
import com.example.webmailcore.repositories.specifications.PrivilegeSpecification;
import com.example.webmailcore.repositories.specifications.SearchCriteria;
import com.example.webmailcore.repositories.specifications.SearchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@Service
public class PrivilegeService {

    @Autowired
    PrivilegeRepository privilegeRepository;

    public Page<Privilege> all(Map<String, String> params, Pageable pageable) {
        PrivilegeSpecification privilegeSpecification = new PrivilegeSpecification();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (!StringUtils.isEmpty(entry.getKey()) && !StringUtils.isEmpty(entry.getValue()))
                privilegeSpecification.add(new SearchCriteria(entry.getKey(), entry.getValue(), SearchOperation.MATCH));
            Date date = new Date();

        }
        return privilegeRepository.findAll(privilegeSpecification, pageable);
    }

    public Privilege get(String id) {
        return privilegeRepository.getById(id);
    }

    public Privilege findByName(String name) {
        return privilegeRepository.findByName(name);
    }

    public Privilege save(Privilege privilege) {
        return privilegeRepository.save(privilege);
    }
}
