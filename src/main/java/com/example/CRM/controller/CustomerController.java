package com.example.CRM.controller;

import com.example.CRM.model.Customer;
import com.example.CRM.model.Device;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.service.CustomerService;
import com.example.CRM.service.DeviceService;
import com.example.CRM.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("customers")
public class CustomerController {

    final
    CustomerService customerService;

    final DeviceService deviceService;

    public CustomerController(CustomerService customerService, DeviceService deviceService) {
        this.customerService = customerService;
        this.deviceService = deviceService;
    }

    @Operation(summary = "Get All Customers")
    @GetMapping
    public PagedResponse<Customer> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort,
            @RequestParam(name = "search", required = false, defaultValue = "") String search){
        return customerService.getAllCustomers(page-1, size, sort, search);
    }

    @Operation(summary = "Get Customer by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id){
        return customerService.getById(id);
    }

    @Operation(summary = "Add Customer")
    @PostMapping
    public ResponseEntity<Customer> addCustomer(@Valid @RequestBody Customer customer){
        return customerService.addCustomer(customer);
    }

    @Operation(summary = "Update Customer")
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer){
        return customerService.updateCustomer(id, customer);
    }

    @Operation(summary = "Get Devices by Customer id")
    @GetMapping("/{id}/devices")
    public PagedResponse<Device> getDevicesByCustomerId(@PathVariable Long id,
             @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
             @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
             @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return deviceService.getDevicesByCustomerId(id, page - 1, size, sort);
    }

    @Operation(summary = "Deactivate Customer")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Customer> deactivateCustomer(@PathVariable Long id){
        return customerService.changeActiveCustomer(id, false);
    }

    @Operation(summary = "Activate Customer")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Customer> activateCustomer(@PathVariable Long id){
        return customerService.changeActiveCustomer(id, true);
    }

    @Operation(summary = "Delete Customer")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable Long id){
        return customerService.deleteCustomer(id);
    }
}
