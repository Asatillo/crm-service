package com.example.CRM.service;

import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.Customer;
import com.example.CRM.model.Subscription;
import com.example.CRM.repository.CustomerRepository;
import com.example.CRM.repository.SubscriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    final
    SubscriptionRepository subscriptionRepository;

    final
    CustomerRepository CustomerRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, CustomerRepository CustomerRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.CustomerRepository = CustomerRepository;
    }

    public ResponseEntity<List<Subscription>> getAll() {
        return new ResponseEntity<>(subscriptionRepository.findAll(), HttpStatus.OK);
    }

    // TODO: data validation
    public ResponseEntity<Subscription> getById(Long id) {
        return new ResponseEntity<>(subscriptionRepository.findById(id).orElse(null), HttpStatus.OK);
    }

    // TODO: data validation
    public ResponseEntity<Subscription> addSubscription(Subscription subscription) {
        return new ResponseEntity<>(subscriptionRepository.save(subscription), HttpStatus.CREATED);
    }

    // TODO: data validation
    public ResponseEntity<List<Subscription>> getSubscriptionsByCustomerId(Long id) {
        Customer customer = CustomerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        return new ResponseEntity<>(subscriptionRepository.findAllByCustomerId(id), HttpStatus.OK);
    }

    // TODO: data validation
    public ResponseEntity<List<Customer>> getCustomersByPlanId(Long id) {
        return new ResponseEntity<>(subscriptionRepository.getCustomersByPlanId(id), HttpStatus.OK);
    }

    // TODO: data validation
    public ResponseEntity<Long> deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
