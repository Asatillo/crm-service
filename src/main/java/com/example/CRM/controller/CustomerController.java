package com.example.CRM.controller;

import com.example.CRM.model.Customer;
import com.example.CRM.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class CustomerController {

    final
    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // TODO: add filters, perpage, page, sortby, sortorder
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAll(){
        return customerService.getAll();
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id){
        return customerService.getById(id);
    }

    @PostMapping("/customer")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer){
        return customerService.addCustomer(customer);
    }

    // change customer info
    @PutMapping("/customer/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer){
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<Long> deleteCustomer(@PathVariable Long id){
        return customerService.deleteCustomer(id);
    }
}
