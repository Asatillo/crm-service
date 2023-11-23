package com.example.CRM.controller;

import com.example.CRM.model.Device;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.DeviceRequest;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.service.DeviceService;
import com.example.CRM.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.CRM.utils.AppConstants.API_URL;

@RestController
@RequestMapping(API_URL)
public class DeviceController {

    DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Operation(summary = "Get All Devices")
    @GetMapping("/devices")
    public PagedResponse<Device> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort
    ){
        return deviceService.getAllDevices(page - 1, size, sort);
    }

    @Operation(summary = "Get Device by Id")
    @GetMapping("/devices/{id}")
    public ResponseEntity<Device> getById(@PathVariable Long id){
        return deviceService.getById(id);
    }

    @Operation(summary = "Get Device by Device Template id")
    @GetMapping("/devices/template/{id}")
    public PagedResponse<Device> getByDeviceTemplateId(@PathVariable Long id,
           @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
           @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
           @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return deviceService.getByDeviceTemplateId(id, page - 1, size, sort);
    }

    @Operation(summary = "Get Device by Customer id")
    @GetMapping("/customers/{id}/devices")
    public PagedResponse<Device> getByCustomerId(@PathVariable Long id,
           @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
           @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
           @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return deviceService.getByCustomerId(id, page - 1, size, sort);
    }

    @Operation(summary = "Get mobile Devices")
    @GetMapping("/devices/type/{deviceType}")
    public PagedResponse<Device> getMobile(@PathVariable String deviceType,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort
    ){
        return deviceService.getByDeviceType(deviceType, page - 1, size, sort);
    }

    @Operation(summary = "Add Device")
    @PostMapping("/devices")
    public ResponseEntity<Device> addDevice(@Valid @RequestBody DeviceRequest deviceRequest){
        return deviceService.addDevice(deviceRequest);
    }

    @Operation(summary = "Delete Device")
    @DeleteMapping("/devices/{id}")
    public ResponseEntity<ApiResponse> deleteDevice(@PathVariable Long id){
        return deviceService.deleteDevice(id);
    }
}
