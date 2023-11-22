package com.example.CRM.service;

import com.example.CRM.exceptions.InvalidInputException;
import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.*;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.payload.SubscriptionRequest;
import com.example.CRM.repository.*;
import com.example.CRM.utils.AppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SubscriptionService {

    final SubscriptionRepository subscriptionRepository;

    final NetworkEntityRepository networkEntityRepository;

    final PlanRepository planRepository;
    
    final DeviceRepository deviceRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, NetworkEntityRepository networkEntityRepository, PlanRepository planRepository, DeviceRepository deviceRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.networkEntityRepository = networkEntityRepository;
        this.planRepository = planRepository;
        this.deviceRepository = deviceRepository;
    }

    public PagedResponse<Subscription> getAll(int page, int size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, Subscription.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Subscription> subscriptions = subscriptionRepository.findAll(pageable);
        PagedResponse<Subscription> pagedResponse = new PagedResponse<>();

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());

        return pagedResponse.returnPagedResponse(subscriptions);
    }

    public ResponseEntity<Subscription> getById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));

        return new ResponseEntity<>(subscription, HttpStatus.OK);
    }

    public ResponseEntity<Subscription> addSubscription(SubscriptionRequest subscriptionRequest) {
        Long networkEntityId = subscriptionRequest.getNetworkEntity();
        Long planId = subscriptionRequest.getPlanId();
        Long deviceId = subscriptionRequest.getDeviceId();
        LocalDateTime startDate = subscriptionRequest.getStartDate();

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", "id", planId));
        Customer customer = CustomerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        NetworkEntity newNetworkEntity = networkEntityRepository.findById(networkEntityId)
                .orElseThrow(() -> new ResourceNotFoundException("Network Entity", "id", networkEntityId));
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device", "id", deviceId));


        if(!plan.isActive()){
            throw new InvalidInputException(new ApiResponse(Boolean.FALSE, String.format("%s with id value '%s' is inactive", "plan", planId)));
        }

        if(!newNetworkEntity.isActive()){
            throw new InvalidInputException(new ApiResponse(Boolean.FALSE, String.format("%s  '%s' is inactive", "Network Entity", newNetworkEntity.getNetworkIdentifier())));
        }

        if(startDate.isBefore(LocalDateTime.now())){
            throw new InvalidInputException(new ApiResponse(Boolean.FALSE, String.format("%s cannot be before the current date", "start date")));
        }

        Subscription subscription = new Subscription(newNetworkEntity, plan,  device, startDate);
        return new ResponseEntity<>(subscriptionRepository.save(subscription), HttpStatus.CREATED);
    }

    public PagedResponse<Subscription> getSubscriptionsByCustomerId(Long id, int page, int size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, Subscription.class);

        networkEntityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Network Entity", "id", id));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Subscription> subscriptions = subscriptionRepository.findAllByNetworkEntity_Owner_Id(id, pageable);
        PagedResponse<Subscription> pagedResponse = new PagedResponse<>();

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());

        return pagedResponse.returnPagedResponse(subscriptions);
    }

    public PagedResponse<Subscription> getSubscriptionsByPlanId(Long id, Integer page, Integer size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, Subscription.class);

        planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Subscription> subscriptions = subscriptionRepository.findAllByPlanId(id, pageable);
        PagedResponse<Subscription> pagedResponse = new PagedResponse<>();

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());

        return pagedResponse.returnPagedResponse(subscriptions);
    }

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

    public ResponseEntity<ApiResponse> activateSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));
        if(subscription.isActive()){
            return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Subscription is already active"), HttpStatus.OK);
        }
        subscription.setActive(true);
        subscriptionRepository.save(subscription);
        return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Subscription activated successfully"), HttpStatus.OK);
    }
}
