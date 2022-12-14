package com.example.webmailcore.services;

import com.example.webmailcore.models.City;
import com.example.webmailcore.repositories.CityRepository;
import com.example.webmailcore.repositories.specifications.CitySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class CityService {

    @Autowired
    CityRepository repository;

    public Page<City> all(Map<String, String> params, Pageable pageable) {
        CitySpecification citySpecification = new CitySpecification();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        return repository.findAll(citySpecification, pageable);
    }

    public City getById(String id) {
        City city = repository.getById(id);
        return city;
    }
}
