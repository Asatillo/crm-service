package com.example.CRM.controller;

import com.example.CRM.model.Subscription;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.payload.SubscriptionRequest;
import com.example.CRM.service.SubscriptionService;
import com.example.CRM.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/")
public class SubscriptionController {

    final
    SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // TODO: add filters, sortorder
    @Operation(summary = "Get All Subscriptions")
    @GetMapping("/subscriptions")
    public PagedResponse<Subscription> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort
    ){
        return subscriptionService.getAll(page-1, size, sort);
    }

    @Operation(summary = "Get Subscription by Id")
    @GetMapping("/subscription/{id}")
    public ResponseEntity<Subscription> getById(@PathVariable Long id){
        return subscriptionService.getById(id);
    }

    @Operation(summary = "Get Subscriptions by Customer Id")
    @GetMapping("/subscriptions/customer/{id}")
    public PagedResponse<Subscription> getSubscriptionsByCustomerId(@PathVariable Long id,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return subscriptionService.getSubscriptionsByCustomerId(id, page-1, size, sort);
    }

    @Operation(summary = "Get Subscriptions by Plan Id")
    @GetMapping("/subscriptions/plan/{id}")
    public PagedResponse<Subscription> getSubscriptionsByPlanId(@PathVariable Long id,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return subscriptionService.getSubscriptionsByPlanId(id, page-1, size, sort);
    }

    @Operation(summary = "Add Subscription")
    @PostMapping("/subscription")
    public ResponseEntity<Subscription> addSubscription(@Valid @RequestBody SubscriptionRequest subscriptionRequest){
        return subscriptionService.addSubscription(subscriptionRequest);
    }

    @Operation(summary = "Delete Subscription")
    @DeleteMapping("/subscription/{id}")
    public ResponseEntity<ApiResponse> deleteSubscription(@PathVariable Long id){
        return subscriptionService.deleteSubscription(id);
    }

    @Operation(summary = "Deactivate Subscription")
    @PatchMapping("/subscription/{id}/deactivate")
    public ResponseEntity<ApiResponse> deactivateSubscription(@PathVariable Long id){
        return subscriptionService.deactivateSubscription(id);
    }

    @Operation(summary = "Activate Subscription")
    @PatchMapping("/subscription/{id}/activate")
    public ResponseEntity<ApiResponse> activateSubscription(@PathVariable Long id){
        return subscriptionService.activateSubscription(id);
    }
}
