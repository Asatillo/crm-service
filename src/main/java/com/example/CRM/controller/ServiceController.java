package com.example.CRM.controller;

import com.example.CRM.model.Service;
import com.example.CRM.model.enums.DeviceType;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.service.ServiceService;
import com.example.CRM.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Services", description = "Service API")
@RequestMapping("services")
public class ServiceController {

    final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Operation(summary = "Get All Services")
    @GetMapping
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public PagedResponse<Service> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort,
            @RequestParam(name = "search", required = false, defaultValue = "") String search){
        return serviceService.getAll(page-1, size, sort, search);
    }

    @Operation(summary = "Get Service by Id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public ResponseEntity<Service> getById(@PathVariable Long id){
        return serviceService.getById(id);
    }

    @Operation(summary = "Get Services by Designated Device Type")
    @GetMapping("/device-type/{deviceType}")
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public PagedResponse<Service> getServicesByDesignatedDeviceType(@PathVariable DeviceType deviceType,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return serviceService.getServicesByDesignatedDeviceType(deviceType, page-1, size, sort);
    }

    @Operation(summary = "Add Service")
    @PostMapping
    @PreAuthorize("hasAnyRole('admin', 'sales')")
    public ResponseEntity<Service> addService(@Valid @RequestBody Service service){
        return serviceService.addService(service);
    }

    @Operation(summary = "Update Service")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'sales')")
    public ResponseEntity<Service> updateService(@PathVariable Long id, @Valid @RequestBody Service service){
        return serviceService.updateService(id, service);
    }

    @Operation(summary = "Deactivate Service")
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('admin', 'sales')")
    public ResponseEntity<Service> deactivateService(@PathVariable Long id){
        return serviceService.changeActive(id, false);
    }

    @Operation(summary = "Activate Service")
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('admin', 'sales')")
    public ResponseEntity<Service> activateService(@PathVariable Long id){
        return serviceService.changeActive(id, true);
    }

    @Operation(summary = "Delete Service")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin')")
    public ResponseEntity<ApiResponse> deleteService(@PathVariable Long id){
        return serviceService.deleteService(id);
    }

}
