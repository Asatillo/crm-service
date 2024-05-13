package com.example.CRM.controller;

import com.example.CRM.model.Device;
import com.example.CRM.model.enums.DeviceType;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.request.DeviceRequest;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.payload.request.DeviceSellRequest;
import com.example.CRM.service.DeviceService;
import com.example.CRM.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Devices", description = "Device API")
@RequestMapping("devices")
public class DeviceController {

    DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Operation(summary = "Get All Devices")
    @GetMapping
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public PagedResponse<Device> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort
    ){
        return deviceService.getAllDevices(page - 1, size, sort);
    }

    @Operation(summary = "Get Available Devices")
    @GetMapping("/type/{deviceType}/available")
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public PagedResponse<Device> getAvailable(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @PathVariable DeviceType deviceType){
        return deviceService.getAvailableDevices(page - 1, size, sort, search, deviceType);
    }


    @Operation(summary = "Get Device by Id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public ResponseEntity<Device> getById(@PathVariable Long id){
        return deviceService.getById(id);
    }

    @Operation(summary = "Get Device by Device Template id")
    @GetMapping("/template/{id}")
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public PagedResponse<Device> getByDeviceTemplateId(@PathVariable Long id,
           @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
           @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
           @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return deviceService.getByDeviceTemplateId(id, page - 1, size, sort);
    }

    @Operation(summary = "Get Devices by Customer id")
    @GetMapping("/customer/{id}")
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public PagedResponse<Device> getByCustomerId(@PathVariable Long id,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort,
            @RequestParam(name = "search", required = false, defaultValue = "") String search){
        return deviceService.getByCustomerId(id, page - 1, size, sort, search);
    }

    @Operation(summary = "Sell device to Customer")
    @PostMapping("/{id}/sell")
    @PreAuthorize("hasAnyRole('agent', 'admin')")
    public ResponseEntity<Device> sellDevice(@PathVariable Long id, @RequestBody DeviceSellRequest deviceSellRequest,
                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        return deviceService.sellDevice(id, deviceSellRequest, authHeader);
    }

    @Operation(summary = "Get Devices by Type")
    @GetMapping("/type/{deviceType}")
    @PreAuthorize("hasAnyRole('agent', 'admin', 'sales')")
    public PagedResponse<Device> getByDeviceType(@PathVariable DeviceType deviceType,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort,
            @RequestParam(name = "search", required = false, defaultValue = "") String search
    ){
        return deviceService.getByDeviceType(deviceType, page - 1, size, sort, search);
    }

    @Operation(summary = "Add Devices")
    @PostMapping
    @PreAuthorize("hasAnyRole('sales', 'admin')")
    public ResponseEntity<ApiResponse> addDevices(@Valid @RequestBody DeviceRequest deviceRequest){
        return deviceService.addDevices(deviceRequest);
    }

    @Operation(summary = "Update Device")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('sales', 'admin')")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @Valid @RequestBody DeviceRequest deviceRequest){
        return deviceService.updateDevice(id, deviceRequest);
    }

    @Operation(summary = "Delete Device")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin')")
    public ResponseEntity<ApiResponse> deleteDevice(@PathVariable Long id){
        return deviceService.deleteDevice(id);
    }
}
