package com.example.CRM.controller;

import com.example.CRM.model.enums.DeviceType;
import com.example.CRM.model.template.DeviceTemplate;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.service.DeviceTemplateService;
import com.example.CRM.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Device Templates", description = "Device Template API")
@RequestMapping("device-templates")
public class DeviceTemplateController {

    final DeviceTemplateService deviceTemplateService;

    public DeviceTemplateController(DeviceTemplateService deviceTemplateService) {
        this.deviceTemplateService = deviceTemplateService;
    }

    @Operation(summary = "Get All Device Templates")
    @GetMapping
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public PagedResponse<DeviceTemplate> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort,
            @RequestParam(name = "paginate", required = false, defaultValue = "true") Boolean paginate){
        return deviceTemplateService.getAllDeviceTemplates(page-1, size, sort, paginate);
    }

    @Operation(summary = "Get Device Template by Id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'sales')")
    public ResponseEntity<DeviceTemplate> getById(@PathVariable Long id){
        return deviceTemplateService.getById(id);
    }

    @Operation(summary = "Get Device Templates by Brand")
    @GetMapping("/brand/{brand}")
    @PreAuthorize("hasAnyRole('admin', 'sales')")
    public PagedResponse<DeviceTemplate> getByBrand(@PathVariable String brand,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return deviceTemplateService.getByBrand(brand, page-1, size, sort);
    }

    @Operation(summary = "Get Mobile Device Templates By Type")
    @GetMapping("/type/{deviceType}")
    @PreAuthorize("hasAnyRole('admin', 'sales')")
    public PagedResponse<DeviceTemplate> getByType(@PathVariable DeviceType deviceType,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort,
            @RequestParam(name = "search", required = false, defaultValue = "") String search){
        return deviceTemplateService.getByDeviceType(deviceType, page-1, size, sort, search);
    }

    @Operation(summary = "Create Device Template")
    @PostMapping
    @PreAuthorize("hasAnyRole('admin', 'sales')")
    public ResponseEntity<DeviceTemplate> createDeviceTemplate(@Valid @RequestBody DeviceTemplate deviceTemplate){
        return deviceTemplateService.createDeviceTemplate(deviceTemplate);
    }

    @Operation(summary = "Update Device Template")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'sales')")
    public ResponseEntity<DeviceTemplate> updateDeviceTemplate(@PathVariable Long id, @Valid @RequestBody DeviceTemplate deviceTemplate){
        return deviceTemplateService.updateDeviceTemplate(id, deviceTemplate);
    }

    @Operation(summary = "Delete Device Template")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin')")
    public ResponseEntity<ApiResponse> deleteDeviceTemplate(@PathVariable Long id){
        return deviceTemplateService.deleteDeviceTemplate(id);
    }

    @Operation(summary = "Deactivate Device Template")
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('admin', 'sales')")
    public ResponseEntity<ApiResponse> deactivateDeviceTemplate(@PathVariable Long id){
        return deviceTemplateService.deactivateDeviceTemplate(id);
    }

    @Operation(summary = "Activate Device Template")
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('admin', 'sales')")
    public ResponseEntity<ApiResponse> activateDeviceTemplate(@PathVariable Long id){
        return deviceTemplateService.activateDeviceTemplate(id);
    }
}
