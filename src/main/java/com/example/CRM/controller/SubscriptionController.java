package com.example.CRM.controller;

import com.example.CRM.model.Customer;
import com.example.CRM.model.Subscription;
import com.example.CRM.service.SubscriptionService;
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

    // TODO: add filters, perpage, page, sortby, sortorder
    @Operation(summary = "Get All Subscriptions")
    @GetMapping("/subscriptions")
    public ResponseEntity<List<Subscription>> getAll(){
        return subscriptionService.getAll();
    }

    @Operation(summary = "Get Subscription by Id")
    @GetMapping("/subscription/{id}")
    public ResponseEntity<Subscription> getById(@PathVariable Long id){
        return subscriptionService.getById(id);
    }

    @Operation(summary = "Get Subscriptions of Customer Id")
    @GetMapping("/subscriptions/customer/{id}")
    public ResponseEntity<List<Subscription>> getSuscriptionsByCustomerId(@PathVariable Long id){
        return subscriptionService.getSubscriptionsByCustomerId(id);
    }

    // get customers by active plan subscriptions
    @Operation(summary = "Get Customers by Plan Id of Their Subscription")
    @GetMapping("/customers/plan/{id}")
    public ResponseEntity<List<Customer>> getCustomersByPlanId(@PathVariable Long id){
        return subscriptionService.getCustomersByPlanId(id);
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
