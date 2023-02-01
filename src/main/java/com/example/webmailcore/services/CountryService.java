package com.example.webmailcore.services;

import com.example.webmailcore.exceptions.BadRequestError;
import com.example.webmailcore.models.Country;
import com.example.webmailcore.repositories.CountryRepository;
import com.example.webmailcore.repositories.specifications.CountrySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    public Boolean exists(String id) {
        return repository.existsById(id);
    }

    public Country findByCode(String code) {
        return repository.findByCode(code);
    }

    public Country create(Country country) {
        return repository.save(country);
    }

    public Country update(Country country) {
        return repository.save(country);
    }

    public List<Country> saveAll(List<Country> countries) {
        return repository.saveAll(countries);
    }

    public Boolean delete(String id) {
        Country country = repository.getById(id);
        repository.delete(country);
        return true;
    }
}
