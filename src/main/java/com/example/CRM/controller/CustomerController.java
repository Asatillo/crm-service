package com.example.CRM.controller;

import com.example.CRM.model.Customer;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.service.CustomerService;
import com.example.CRM.utils.AppConstants;
import com.example.CRM.utils.AppUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class CustomerController {

    final
    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Get All Customers")
    @GetMapping("/customers")
    public PagedResponse<Customer> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return customerService.getAllCustomers(page, size, sort);
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

    @Operation(summary = "Update Customer")
    @PutMapping("/customer/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer){
        return customerService.updateCustomer(id, customer);
    }

    @Operation(summary = "Deactivate Customer")
    @PatchMapping("/customer/{id}/deactivate")
    public ResponseEntity<ApiResponse> deactivateCustomer(@PathVariable Long id){
        return customerService.deactivateCustomer(id);
    }

    @Operation(summary = "Delete Customer")
    @DeleteMapping("/customer/{id}")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable Long id){
        return customerService.deleteCustomer(id);
    }
}
