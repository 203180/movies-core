    package com.example.webmailcore.services;

import com.example.webmailcore.models.DestinationCity;
import com.example.webmailcore.repositories.DestinationCityRepository;
import com.example.webmailcore.repositories.specifications.DestinationCitySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class DestinationCityService {

    @Autowired
    private DestinationCityRepository repository;

    public List<DestinationCity> getAll() {
        return repository.findAll();
    }

    public Page<DestinationCity> all(Map<String, String> params, Pageable pageable) {
        DestinationCitySpecification destinationCitiesSpecification = new DestinationCitySpecification();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        return repository.findAll(destinationCitiesSpecification, pageable);
    }

    public DestinationCity getById(String id) {
        DestinationCity destinationCity = repository.getById(id);
        return destinationCity;
    }
}
