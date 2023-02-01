package com.example.webmailcore.services;

import com.example.webmailcore.models.AirplaneCompany;
import com.example.webmailcore.models.Client;
import com.example.webmailcore.models.Country;
import com.example.webmailcore.repositories.ClientRepository;
import com.example.webmailcore.repositories.specifications.AirplaneCompanySpecification;
import com.example.webmailcore.repositories.specifications.ClientSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    ClientRepository repository;

    public List<Client> getAll() {
        return repository.findAll();
    }

    public Page<Client> all(Map<String, String> params, Pageable pageable) {
        ClientSpecification specification = new ClientSpecification();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        return repository.findAll(specification, pageable);
    }

    public Client save(Client client) {
        return repository.save(client);
    }

    public Client getById(String id) {
        Client client = repository.getById(id);
        return client;
    }

    public Client create(Client client) {
        return repository.save(client);
    }

    public Client update(Client client) {
        return repository.save(client);
    }

    public Boolean delete(String id) {
        Client client = repository.getById(id);
        repository.delete(client);
        return true;
    }
}
