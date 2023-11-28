package com.example.CRM.controller;

import com.example.CRM.model.Customer;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.service.CustomerService;
import com.example.CRM.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
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
        return customerService.getAllCustomers(page-1, size, sort);
    }

    @Operation(summary = "Get Customer by Id")
    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id){
        return customerService.getById(id);
    }

    @Operation(summary = "Add Customer")
    @PostMapping("/customers")
    public ResponseEntity<Customer> addCustomer(@Valid @RequestBody Customer customer){
        return customerService.addCustomer(customer);
    }

    @Operation(summary = "Update Customer")
    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer){
        return customerService.updateCustomer(id, customer);
    }

    @Operation(summary = "Deactivate Customer")
    @PatchMapping("/customers/{id}/deactivate")
    public ResponseEntity<ApiResponse> deactivateCustomer(@PathVariable Long id){
        return customerService.deactivateCustomer(id);
    }

    @Operation(summary = "Activate Customer")
    @PatchMapping("/customers/{id}/activate")
    public ResponseEntity<ApiResponse> activateCustomer(@PathVariable Long id){
        return customerService.activateCustomer(id);
    }

    @Operation(summary = "Delete Customer")
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable Long id){
        return customerService.deleteCustomer(id);
    }
}
