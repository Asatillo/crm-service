package com.example.CRM.controller;

import com.example.CRM.model.Subscription;
import com.example.CRM.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class SubscriptionController {

    final
    SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/subscriptions")
    public List<Subscription> getAll(){
        return subscriptionService.getAll();
    }

    @GetMapping("/subscription/{id}")
    public Subscription getById(@PathVariable Long id){
        return subscriptionService.getById(id);
    }
}
