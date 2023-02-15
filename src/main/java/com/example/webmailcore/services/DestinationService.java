package com.example.webmailcore.services;

import com.example.webmailcore.models.Destination;
import com.example.webmailcore.repositories.DestinationRepository;
import com.example.webmailcore.repositories.specifications.DestinationSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class DestinationService {

    @Autowired
    DestinationRepository repository;

    public List<Destination> getAll() {
        return repository.findAll();
    }

    public Page<Destination> all(Map<String, String> params, Pageable pageable) {
        DestinationSpecification destinationSpecification = new DestinationSpecification();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        return repository.findAll(destinationSpecification, pageable);
    }

    public Destination getById(String id) {
        Destination destination = repository.getById(id);
        return destination;
    }

    public Destination create(Destination destination) {
        return repository.save(destination);
    }

    public Destination update(Destination destination) {
        return repository.save(destination);
    }

    public Boolean delete(String id) {
        Destination destination = repository.getById(id);
        repository.delete(destination);
        return true;
    }

}
