package com.example.CRM.controller;

import com.example.CRM.model.Subscription;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.service.SubscriptionService;
import com.example.CRM.utils.AppConstants;
import com.example.CRM.utils.AppUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        AppUtils.validatePageNumberAndSize(page, size);
        return subscriptionService.getAll(page, size, sort);
    }

    @Operation(summary = "Get Subscription by Id")
    @GetMapping("/subscription/{id}")
    public ResponseEntity<Subscription> getById(@PathVariable Long id){
        return subscriptionService.getById(id);
    }

    @Operation(summary = "Get Subscriptions of Customer Id")
    @GetMapping("/subscriptions/customer/{id}")
    public PagedResponse<Subscription> getSubscriptionsByCustomerId(@PathVariable Long id,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        AppUtils.validatePageNumberAndSize(page, size);
        return subscriptionService.getSubscriptionsByCustomerId(id, page, size, sort);
    }

    @Operation(summary = "Get Subscriptions of Plan Id")
    @GetMapping("/subscriptions/plan/{id}")
    public PagedResponse<Subscription> getSubscriptionsByPlanId(@PathVariable Long id,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        AppUtils.validatePageNumberAndSize(page, size);
        return subscriptionService.getSubscriptionsByPlanId(id, page, size, sort);
    }

    @Operation(summary = "Add Subscription")
    @PostMapping("/subscription")
    public ResponseEntity<Subscription> addSubscription(@RequestBody Subscription subscription){
        return subscriptionService.addSubscription(subscription);
    }

    @Operation(summary = "Delete Subscription")
    @DeleteMapping("/subscription/{id}")
    public ResponseEntity<Long> deleteSubscription(@PathVariable Long id){
        return subscriptionService.deleteSubscription(id);
    }
}
