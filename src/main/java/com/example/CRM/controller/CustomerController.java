package com.example.CRM.controller;

import com.example.CRM.model.Customer;
import com.example.CRM.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Get All Customers")
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAll(){
        return customerService.getAll();
    }

    @Operation(summary = "Get Customer by Id")
    @GetMapping("/customer/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id){
        return customerService.getById(id);
    }

    @Operation(summary = "Add Customer")
    @PostMapping("/customer")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer){
        return customerService.addCustomer(customer);
    }

    // change customer info
    @Operation(summary = "Update Customer")
    @PutMapping("/customer/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer){
        return customerService.updateCustomer(id, customer);
    }

    @Operation(summary = "Delete Customer")
    @DeleteMapping("/customer/{id}")
    public ResponseEntity<Long> deleteCustomer(@PathVariable Long id){
        return customerService.deleteCustomer(id);
    }
}
