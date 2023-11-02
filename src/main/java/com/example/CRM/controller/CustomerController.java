package com.example.CRM.controller;

import com.example.CRM.model.Customer;
import com.example.CRM.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class CustomerController {

    final
    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<Customer> getAll(){
        return customerService.getAll();
    }

    @GetMapping("/customer/{id}")
    public Customer getById(@PathVariable Long id){
        return customerService.getById(id);
    }

}
