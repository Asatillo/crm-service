package com.example.CRM.service;

import com.example.CRM.exceptions.InvalidInputException;
import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.feign.SalesInterface;
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

import java.time.LocalDate;
import java.util.HashMap;

@Service
public class SubscriptionService {

    final SubscriptionRepository subscriptionRepository;

    final NetworkEntityRepository networkEntityRepository;

    final PlanRepository planRepository;

    final DeviceRepository deviceRepository;

    SalesInterface salesInterface;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               NetworkEntityRepository networkEntityRepository, PlanRepository planRepository,
                               DeviceRepository deviceRepository, SalesInterface salesInterface) {
        this.subscriptionRepository = subscriptionRepository;
        this.networkEntityRepository = networkEntityRepository;
        this.planRepository = planRepository;
        this.deviceRepository = deviceRepository;
        this.salesInterface = salesInterface;
    }

    public PagedResponse<Subscription> getAll(int page, int size, String sort, String search) {
        AppUtils.validatePaginationRequestParams(page, size, sort, Subscription.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Subscription> subscriptions = subscriptionRepository.findAllWithSearch(pageable, search);
        PagedResponse<Subscription> pagedResponse = new PagedResponse<>(subscriptions);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public ResponseEntity<Subscription> getById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));

        return new ResponseEntity<>(subscription, HttpStatus.OK);
    }

    public ResponseEntity<Subscription> addSubscription(@NonNull SubscriptionRequest subscriptionRequest,
                                                        String authorizationHeader) {
        Long networkEntityId = subscriptionRequest.getNetworkEntity();
        Long planId = subscriptionRequest.getPlanId();
        LocalDate startDate = subscriptionRequest.getStartDate();

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", "id", planId));
        NetworkEntity networkEntity = networkEntityRepository.findById(networkEntityId)
                .orElseThrow(() -> new ResourceNotFoundException("Network Entity", "id", networkEntityId));

        if(!plan.isActive()){
            throw new InvalidInputException(new ApiResponse(false, String.format("%s with id value '%s' is inactive", "plan", planId)));
        }

        if(!networkEntity.isActive()){
            throw new InvalidInputException(new ApiResponse(false, String.format("%s  '%s' is inactive", "Network Entity", networkEntity.getNetworkIdentifier())));
        }

        if(startDate.isBefore(LocalDate.now())){
            throw new InvalidInputException(new ApiResponse(false, "start date cannot be before the current date"));
        }

        if(!plan.getDesignatedDeviceType().equals(networkEntity.getDeviceType())){
            throw new InvalidInputException(new ApiResponse(false, String.format("Plan with id value '%s' is not designated to device type of Network Entity with id value '%s'", planId, networkEntityId)));
        }

        if(plan.getDesignatedDeviceType().equals("ROUTER") && !networkEntity.getOwner().isWiredInternetAvailable()){
            throw new InvalidInputException(new ApiResponse(false, "Wired internet is not available in this area"));
        }

        HashMap<String, Object> sale = getSale(plan, networkEntity.getOwner(), subscriptionRequest.getPromotionId());

        ResponseEntity<HashMap> saleResponse = salesInterface.add(authorizationHeader, sale);
        if(!saleResponse.getStatusCode().equals(HttpStatus.CREATED)){
            throw new InvalidInputException(new ApiResponse(false, "Sale creation failed"));
        }

        Subscription subscription = new Subscription(networkEntity, plan, startDate);
        return new ResponseEntity<>(subscriptionRepository.save(subscription), HttpStatus.CREATED);
    }

    private static HashMap<String, Object> getSale(Plan plan, Customer customer, Long promotionId) {
        HashMap<String, Object> sale = new HashMap();
        if(promotionId != null){
            sale.put("promotionId", promotionId);
        }
        sale.put("description", String.format("Plan %s sold to %s", plan.getName(), customer.getFullName()));
        sale.put("customerId", customer.getId());
        sale.put("customer", customer.getFullName());
        sale.put("productId", plan.getId());
        sale.put("productType", "PLAN");
        sale.put("amount", plan.getPrice());
        return sale;
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

    public ResponseEntity<Subscription> changeActive(Long id, boolean active) {
        Subscription subscription = subscriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", id));
        subscription.setActive(active);
        subscriptionRepository.save(subscription);
        return new ResponseEntity<>(subscription, HttpStatus.OK);
    }
}
