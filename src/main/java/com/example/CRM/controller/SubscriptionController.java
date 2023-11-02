package com.example.CRM.controller;

import com.example.CRM.model.Customer;
import com.example.CRM.model.Subscription;
import com.example.CRM.service.SubscriptionService;
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
    @GetMapping("/subscriptions")
    public ResponseEntity<List<Subscription>> getAll(){
        return subscriptionService.getAll();
    }

    @GetMapping("/subscription/{id}")
    public ResponseEntity<Subscription> getById(@PathVariable Long id){
        return subscriptionService.getById(id);
    }

    @GetMapping("/subscriptions/customer/{id}")
    public ResponseEntity<List<Subscription>> getSuscriptionsByCustomerId(@PathVariable Long id){
        return subscriptionService.getSubscriptionsByCustomerId(id);
    }

    // get customers by active plan subscriptions
    @GetMapping("/customers/plan/{id}")
    public ResponseEntity<List<Customer>> getCustomersByPlanId(@PathVariable Long id){
        return subscriptionService.getCustomersByPlanId(id);
    }

    @PostMapping("/subscription")
    public ResponseEntity<Subscription> addSubscription(@RequestBody Subscription subscription){
        return subscriptionService.addSubscription(subscription);
    }
}
