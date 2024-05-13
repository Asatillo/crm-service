package com.example.CRM.controller;

import com.example.CRM.model.Subscription;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.payload.request.SubscriptionRequest;
import com.example.CRM.service.SubscriptionService;
import com.example.CRM.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Subscriptions", description = "Subscription API")
@RequestMapping("subscriptions")
public class SubscriptionController {

    final
    SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Operation(summary = "Get All Subscriptions")
    @GetMapping
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public PagedResponse<Subscription> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            Authentication authentication){
        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_admin")) || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_sales"))) {
            return subscriptionService.getAll(page - 1, size, sort, search, "");
        }else{
            String city = ((JwtAuthenticationToken) authentication).getToken().getClaimAsString("city");
            return subscriptionService.getAll(page-1, size, sort, search, city);
        }
    }

    @Operation(summary = "Get Subscription by Id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public ResponseEntity<Subscription> getById(@PathVariable Long id){
        return subscriptionService.getById(id);
    }

    @Operation(summary = "Get Subscriptions by Customer Id")
    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public PagedResponse<Subscription> getSubscriptionsByCustomerId(@PathVariable Long id,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return subscriptionService.getSubscriptionsByCustomerId(id, page-1, size, sort);
    }

    @Operation(summary = "Get Subscriptions by Plan Id")
    @GetMapping("/plan/{id}")
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public PagedResponse<Subscription> getSubscriptionsByPlanId(@PathVariable Long id,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return subscriptionService.getSubscriptionsByPlanId(id, page-1, size, sort);
    }

    @Operation(summary = "Add Subscription")
    @PostMapping
    @PreAuthorize("hasAnyRole('admin', 'agent')")
    public ResponseEntity<Subscription> addSubscription(@Valid @RequestBody SubscriptionRequest subscriptionRequest,
                                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        return subscriptionService.addSubscription(subscriptionRequest, authHeader);
    }

    @Operation(summary = "Delete Subscription")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin')")
    public ResponseEntity<ApiResponse> deleteSubscription(@PathVariable Long id){
        return subscriptionService.deleteSubscription(id);
    }

    @Operation(summary = "Deactivate Subscription")
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('admin')")
    public ResponseEntity<Subscription> deactivateSubscription(@PathVariable Long id){
        return subscriptionService.changeActive(id, false);
    }

    @Operation(summary = "Activate Subscription")
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('admin')")
    public ResponseEntity<Subscription> activateSubscription(@PathVariable Long id){
        return subscriptionService.changeActive(id, true);
    }
}
