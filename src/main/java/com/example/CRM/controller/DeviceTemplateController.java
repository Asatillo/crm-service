package com.example.CRM.controller;

import com.example.CRM.model.template.DeviceTemplate;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.service.DeviceTemplateService;
import com.example.CRM.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class DeviceTemplateController {

    final DeviceTemplateService deviceTemplateService;

    public DeviceTemplateController(DeviceTemplateService deviceTemplateService) {
        this.deviceTemplateService = deviceTemplateService;
    }

    @Operation(summary = "Get All Device Templates")
    @GetMapping("/device-templates")
    public PagedResponse<DeviceTemplate> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return deviceTemplateService.getAllDeviceTemplates(page-1, size, sort);
    }

    @Operation(summary = "Get Device Template by Id")
    @GetMapping("/device-templates/{id}")
    public ResponseEntity<DeviceTemplate> getById(@PathVariable Long id){
        return deviceTemplateService.getById(id);
    }

    @Operation(summary = "Get Device Templates by Brand")
    @GetMapping("/device-templates/brand/{brand}")
    public PagedResponse<DeviceTemplate> getByBrand(@PathVariable String brand,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return deviceTemplateService.getByBrand(brand, page-1, size, sort);
    }

    @Operation(summary = "Get Mobile Device Templates")
    @GetMapping("/device-templates/type/{deviceType}")
    public PagedResponse<DeviceTemplate> getByType(@PathVariable String deviceType,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return deviceTemplateService.getByDeviceType(deviceType, page-1, size, sort);
    }

    @Operation(summary = "Create Device Template")
    @PostMapping("/device-templates")
    public ResponseEntity<DeviceTemplate> createDeviceTemplate(@Valid @RequestBody DeviceTemplate deviceTemplate){
        return deviceTemplateService.createDeviceTemplate(deviceTemplate);
    }

    @Operation(summary = "Update Device Template")
    @PutMapping("/device-templates/{id}")
    public ResponseEntity<DeviceTemplate> updateDeviceTemplate(@PathVariable Long id, @Valid @RequestBody DeviceTemplate deviceTemplate){
        return deviceTemplateService.updateDeviceTemplate(id, deviceTemplate);
    }

    @Operation(summary = "Delete Device Template")
    @DeleteMapping("/device-templates/{id}")
    public ResponseEntity<ApiResponse> deleteDeviceTemplate(@PathVariable Long id){
        return deviceTemplateService.deleteDeviceTemplate(id);
    }

    @Operation(summary = "Deactivate Device Template")
    @PatchMapping("/device-templates/{id}/deactivate")
    public ResponseEntity<ApiResponse> deactivateDeviceTemplate(@PathVariable Long id){
        return deviceTemplateService.deactivateDeviceTemplate(id);
    }

    @Operation(summary = "Activate Device Template")
    @PatchMapping("/device-templates/{id}/activate")
    public ResponseEntity<ApiResponse> activateDeviceTemplate(@PathVariable Long id){
        return deviceTemplateService.activateDeviceTemplate(id);
    }
}
