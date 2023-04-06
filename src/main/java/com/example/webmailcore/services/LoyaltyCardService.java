package com.example.webmailcore.services;

import com.example.webmailcore.models.LoyaltyCard;
import com.example.webmailcore.repositories.LoyaltyCardRepository;
import com.example.webmailcore.repositories.specifications.LoyaltyCardSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class LoyaltyCardService {

    @Autowired
    LoyaltyCardRepository repository;

    public List<LoyaltyCard> getAll() {
        return repository.findAll();
    }

    public Page<LoyaltyCard> all(Map<String, String> params, Pageable pageable) {
        LoyaltyCardSpecification loyaltyCardSpecification = new LoyaltyCardSpecification();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        return repository.findAll(loyaltyCardSpecification, pageable);
    }

    public LoyaltyCard getById(String id) {
        LoyaltyCard loyaltyCard = repository.getById(id);
        return loyaltyCard;
    }

    public Boolean exists(String id) {
        return repository.existsById(id);
    }

    public LoyaltyCard create(LoyaltyCard loyaltyCard) {
        return repository.save(loyaltyCard);
    }

    public LoyaltyCard update(LoyaltyCard loyaltyCard) {
        return repository.save(loyaltyCard);
    }

    public List<LoyaltyCard> saveAll(List<LoyaltyCard> loyaltyCards) {
        return repository.saveAll(loyaltyCards);
    }

    public Boolean delete(String id) {
        LoyaltyCard loyaltyCard = repository.getById(id);
        repository.delete(loyaltyCard);
        return true;
    }

}