package com.example.CRM.controller;

import com.example.CRM.model.Customer;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.service.CustomerService;
import com.example.CRM.service.DeviceService;
import com.example.CRM.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Customers", description = "Customer API")
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
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    @GetMapping
    public PagedResponse<Customer> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            Authentication authentication){
        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_admin")) || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_sales"))){
            return customerService.getAllCustomers(page-1, size, sort, search, "");
        }else{
            String city = ((JwtAuthenticationToken) authentication).getToken().getClaimAsString("city");
            return customerService.getAllCustomers(page-1, size, sort, search, city);
        }
    }

    @Operation(summary = "Get Customer by Id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public ResponseEntity<Customer> getById(@PathVariable Long id){
        return customerService.getById(id);
    }

    @Operation(summary = "Get Customers Full Name by Id")
    @GetMapping("/{id}/fullname")
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public ResponseEntity<String> getCustomerNameById(@PathVariable Long id){
        return customerService.getCustomerNameById(id);
    }

    @Operation(summary = "Add Customer")
    @PostMapping
    @PreAuthorize("hasRole('admin') or (hasRole('agent') and #customer.city == authentication.credentials.claims.get('city'))")
    public ResponseEntity<Customer> addCustomer(@Valid @RequestBody Customer customer){
        return customerService.addCustomer(customer);
    }

    @Operation(summary = "Update Customer")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin') or (hasRole('agent') and #customer.city == authentication.credentials.claims.get('city'))")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer){
        return customerService.updateCustomer(id, customer);
    }

    @Operation(summary = "Deactivate Customer")
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('agent', 'admin')")
    public ResponseEntity<Customer> deactivateCustomer(@PathVariable Long id){
        return customerService.changeActiveCustomer(id, false);
    }

    @Operation(summary = "Activate Customer")
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('agent', 'admin')")
    public ResponseEntity<Customer> activateCustomer(@PathVariable Long id){
        return customerService.changeActiveCustomer(id, true);
    }

    @Operation(summary = "Delete Customer")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin')")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable Long id){
        return customerService.deleteCustomer(id);
    }
}
