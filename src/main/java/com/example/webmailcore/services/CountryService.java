package com.example.webmailcore.services;

import com.example.webmailcore.models.Country;
import com.example.webmailcore.repositories.CountryRepository;
import com.example.webmailcore.repositories.specifications.CountrySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class CountryService {

    @Autowired
    CountryRepository repository;

    public List<Country> getAll() {
        return repository.findAll();
    }

    public Page<Country> all(Map<String, String> params, Pageable pageable) {
        CountrySpecification countrySpecification = new CountrySpecification();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        return repository.findAll(countrySpecification, pageable);
    }

    public Country getById(String id) {
        Country country = repository.getById(id);
        return country;
    }
}
