package com.example.CRM.service;

import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.Subscription;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.repository.CustomerRepository;
import com.example.CRM.repository.PlanRepository;
import com.example.CRM.repository.SubscriptionRepository;
import com.example.CRM.utils.AppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    final
    PlanRepository planRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, CustomerRepository CustomerRepository, PlanRepository planRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.CustomerRepository = CustomerRepository;
        this.planRepository = planRepository;
    }

    public PagedResponse<Subscription> getAll(int page, int size, String sort) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);

        Page<Subscription> subscriptions = subscriptionRepository.findAll(pageable);

        if(subscriptions.getNumberOfElements() == 0){
            return new PagedResponse<>(List.of(), subscriptions.getNumber(), subscriptions.getSize(),
                    subscriptions.getTotalElements(), subscriptions.getTotalPages(), subscriptions.isLast());
        }

        List<Subscription> subscriptionsList = subscriptions.getContent();

        return new PagedResponse<>(subscriptionsList, subscriptions.getNumber(), subscriptions.getSize(), subscriptions.getTotalElements(),
                subscriptions.getTotalPages(), subscriptions.isLast());
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
    public PagedResponse<Subscription> getSubscriptionsByCustomerId(Long id, int page, int size, String sort) {
        CustomerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);

        Page<Subscription> subscriptions = subscriptionRepository.findAllByCustomerId(id, pageable);

        if(subscriptions.getNumberOfElements() == 0){
            return new PagedResponse<>(List.of(), subscriptions.getNumber(), subscriptions.getSize(),
                    subscriptions.getTotalElements(), subscriptions.getTotalPages(), subscriptions.isLast());
        }

        List<Subscription> subscriptionsList = subscriptions.getContent();

        return new PagedResponse<>(subscriptionsList, subscriptions.getNumber(), subscriptions.getSize(), subscriptions.getTotalElements(),
                subscriptions.getTotalPages(), subscriptions.isLast());
    }

    public PagedResponse<Subscription> getSubscriptionsByPlanId(Long id, Integer page, Integer size, String sort) {
        planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);

        Page<Subscription> subscriptions = subscriptionRepository.findAllByPlanId(id, pageable);

        if(subscriptions.getNumberOfElements() == 0){
            return new PagedResponse<>(List.of(), subscriptions.getNumber(), subscriptions.getSize(),
                    subscriptions.getTotalElements(), subscriptions.getTotalPages(), subscriptions.isLast());
        }

        List<Subscription> subscriptionsList = subscriptions.getContent();

        return new PagedResponse<>(subscriptionsList, subscriptions.getNumber(), subscriptions.getSize(), subscriptions.getTotalElements(),
                subscriptions.getTotalPages(), subscriptions.isLast());
    }

    // TODO: data validation
    public ResponseEntity<Long> deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
