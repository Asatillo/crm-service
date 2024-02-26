package com.example.CRM.service;

import com.example.CRM.exceptions.InvalidInputException;
import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.*;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.payload.request.SubscriptionRequest;
import com.example.CRM.repository.*;
import com.example.CRM.utils.AppUtils;
import lombok.NonNull;
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
        PagedResponse<Subscription> pagedResponse = new PagedResponse<>(subscriptions);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public ResponseEntity<Subscription> getById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));

        return new ResponseEntity<>(subscription, HttpStatus.OK);
    }

    public ResponseEntity<Subscription> addSubscription(@NonNull SubscriptionRequest subscriptionRequest) {
        Long networkEntityId = subscriptionRequest.getNetworkEntity();
        Long planId = subscriptionRequest.getPlanId();
        Long deviceId = subscriptionRequest.getDeviceId();
        LocalDateTime startDate = subscriptionRequest.getStartDate();

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", "id", planId));
        NetworkEntity networkEntity = networkEntityRepository.findById(networkEntityId)
                .orElseThrow(() -> new ResourceNotFoundException("Network Entity", "id", networkEntityId));

        if(deviceId != null){
            Device device = deviceRepository.findById(deviceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Device", "id", deviceId));
            if(device.isOwned()){
                throw new InvalidInputException(new ApiResponse(false, "Device is already owned"));
            }
            device.setOwner(networkEntity.getOwner());
        }

        if(!plan.isActive()){
            throw new InvalidInputException(new ApiResponse(false, String.format("%s with id value '%s' is inactive", "plan", planId)));
        }

        if(!networkEntity.isActive()){
            throw new InvalidInputException(new ApiResponse(false, String.format("%s  '%s' is inactive", "Network Entity", networkEntity.getNetworkIdentifier())));
        }

        if(startDate.isBefore(LocalDateTime.now())){
            throw new InvalidInputException(new ApiResponse(false, String.format("%s cannot be before the current date", "start date")));
        }

        if(!plan.getDesignatedDeviceType().equals(networkEntity.getDeviceType())){
            throw new InvalidInputException(new ApiResponse(false, String.format("Plan with id value '%s' is not designated to device type of Network Entity with id value '%s'", planId, networkEntityId)));
        }

        if(plan.getDesignatedDeviceType().equals("ROUTER") && !networkEntity.getOwner().isWiredInternetAvailable()){
            throw new InvalidInputException(new ApiResponse(false, "Wired internet is not available in this area"));
        }

        Device device = null;
        if(deviceId != null){
            device = deviceRepository.findById(deviceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Device", "id", deviceId));
        }

        // if device is null and plan is designated to router, throw error
        if(device == null && plan.getDesignatedDeviceType().equals("ROUTER")){
            throw new InvalidInputException(new ApiResponse(false, "Router device is required for this Plan"));
        }
        // if device is not null and plan is designated to router, check if device is router
        if(device != null && plan.getDesignatedDeviceType().equals("ROUTER") && !device.getDeviceTemplate().getDeviceType().equals("ROUTER")){
                throw new InvalidInputException(new ApiResponse(false, "Router device is required for this Plan"));
        }

        Subscription subscription = new Subscription(networkEntity, plan, device, startDate);
        return new ResponseEntity<>(subscriptionRepository.save(subscription), HttpStatus.CREATED);
    }

    public PagedResponse<Subscription> getSubscriptionsByCustomerId(Long id, int page, int size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, Subscription.class);

        networkEntityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Network Entity", "id", id));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Subscription> subscriptions = subscriptionRepository.findAllByNetworkEntity_Owner_Id(id, pageable);
        PagedResponse<Subscription> pagedResponse = new PagedResponse<>(subscriptions);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public PagedResponse<Subscription> getSubscriptionsByPlanId(Long id, Integer page, Integer size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, Subscription.class);

        planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Subscription> subscriptions = subscriptionRepository.findAllByPlanId(id, pageable);
        PagedResponse<Subscription> pagedResponse = new PagedResponse<>(subscriptions);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public ResponseEntity<ApiResponse> deleteSubscription(Long id) {
        subscriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));
        subscriptionRepository.deleteById(id);
        return new ResponseEntity<>(new ApiResponse(true, "Subscription deleted successfully"), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deactivateSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));
        if(!subscription.isActive()){
            return new ResponseEntity<>(new ApiResponse(false, "Subscription is already deactivated"), HttpStatus.OK);
        }
        subscription.setActive(false);
        subscriptionRepository.save(subscription);
        return new ResponseEntity<>(new ApiResponse(true, "Subscription deactivated successfully"), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> activateSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));
        if(subscription.isActive()){
            return new ResponseEntity<>(new ApiResponse(false, "Subscription is already active"), HttpStatus.OK);
        }
        subscription.setActive(true);
        subscriptionRepository.save(subscription);
        return new ResponseEntity<>(new ApiResponse(true, "Subscription activated successfully"), HttpStatus.OK);
    }
}
