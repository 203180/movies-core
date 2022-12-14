package com.example.webmailcore.services;

import com.example.webmailcore.models.DestinationRegion;
import com.example.webmailcore.repositories.DestinationRegionRepository;
import com.example.webmailcore.repositories.specifications.DestinationRegionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class DestinationRegionService {

    @Autowired
    private DestinationRegionRepository repository;

    public List<DestinationRegion> getAll() {
        return repository.findAll();
    }

    public Page<DestinationRegion> all(Map<String, String> params, Pageable pageable) {
        DestinationRegionSpecification destinationRegionSpecification = new DestinationRegionSpecification();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        return repository.findAll(destinationRegionSpecification, pageable);
    }

    public DestinationRegion getById(String id) {
        DestinationRegion destinationRegion = repository.getById(id);
        return destinationRegion;
    }
}
