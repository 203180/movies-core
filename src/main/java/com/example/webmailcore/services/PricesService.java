package com.example.webmailcore.services;

import com.example.webmailcore.models.Prices;
import com.example.webmailcore.repositories.PricesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PricesService {

    @Autowired
    PricesRepository repository;

    public List<Prices> getAll() {
        return repository.findAll();
    }

    public Prices getById(String id) {
        Prices price = repository.getById(id);
        return price;
    }

}
