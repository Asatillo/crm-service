package com.example.CRM.service;

import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.Subscription;
import com.example.CRM.payload.ApiResponse;
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
        AppUtils.validatePageNumberAndSize(page, size, sort);
        AppUtils.validateSortFieldExists(sort, Subscription.class);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);

        Page<Subscription> subscriptions = subscriptionRepository.findAll(pageable);

        PagedResponse<Subscription> pagedResponse = new PagedResponse<>();
        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());
        return pagedResponse.returnPagedResponse(subscriptions);
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
        AppUtils.validatePageNumberAndSize(page, size, sort);
        AppUtils.validateSortFieldExists(sort, Subscription.class);
        CustomerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);

        Page<Subscription> subscriptions = subscriptionRepository.findAllByCustomerId(id, pageable);

        PagedResponse<Subscription> pagedResponse = new PagedResponse<>();
        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());
        return pagedResponse.returnPagedResponse(subscriptions);
    }

    public PagedResponse<Subscription> getSubscriptionsByPlanId(Long id, Integer page, Integer size, String sort) {
        AppUtils.validatePageNumberAndSize(page, size, sort);
        AppUtils.validateSortFieldExists(sort, Subscription.class);
        planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);

        Page<Subscription> subscriptions = subscriptionRepository.findAllByPlanId(id, pageable);

        PagedResponse<Subscription> pagedResponse = new PagedResponse<>();
        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());
        return pagedResponse.returnPagedResponse(subscriptions);
    }

    // TODO: data validation
    public ResponseEntity<ApiResponse> deleteSubscription(Long id) {
        subscriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));
        subscriptionRepository.deleteById(id);
        return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Subscription deleted successfully"), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deactivateSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));
        if(!subscription.isActive()){
            return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Subscription is already deactivated"), HttpStatus.OK);
        }
        subscription.setActive(false);
        subscriptionRepository.save(subscription);
        return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Subscription deactivated successfully"), HttpStatus.OK);
    }
}
