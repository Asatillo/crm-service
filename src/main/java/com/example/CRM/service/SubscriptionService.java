package com.example.CRM.service;

import com.example.CRM.model.Subscription;
import com.example.CRM.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    public List<Subscription> getAll() {
        return subscriptionRepository.findAll();
    }

    public Subscription getById(Long id) {
        return subscriptionRepository.findById(id).orElse(null);
    }
}
