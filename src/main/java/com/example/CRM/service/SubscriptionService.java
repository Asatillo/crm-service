package com.example.CRM.service;

import com.example.CRM.model.Subscription;
import com.example.CRM.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    final
    SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public ResponseEntity<List<Subscription>> getAll() {
        return new ResponseEntity<>(subscriptionRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Subscription> getById(Long id) {
        return new ResponseEntity<>(subscriptionRepository.findById(id).orElse(null), HttpStatus.OK);
    }
}
