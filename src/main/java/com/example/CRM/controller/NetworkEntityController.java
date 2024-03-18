package com.example.CRM.controller;

import com.example.CRM.model.NetworkEntity;
import com.example.CRM.model.enums.DeviceType;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.request.NetworkEntityRequest;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.payload.request.NetworkEntitySellRequest;
import com.example.CRM.service.NetworkEntityService;
import com.example.CRM.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("network-entities")
public class NetworkEntityController {

    final NetworkEntityService networkEntityService;

    public NetworkEntityController(NetworkEntityService networkEntityService) {
        this.networkEntityService = networkEntityService;
    }

    @Operation(summary = "Get All Network Entities")
    @GetMapping
    public PagedResponse<NetworkEntity> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort,
            @RequestParam(name = "search", required = false, defaultValue = "") String search
    ){
        return networkEntityService.getAll(page-1, size, sort, search);
    }

    @Operation(summary = "Get All Router Network Entities")
    @GetMapping("/device-type/{deviceType}")
    public PagedResponse<NetworkEntity> getAllRouters(@PathVariable DeviceType deviceType,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort
    ){
        return networkEntityService.getAllByDeviceType(deviceType, page-1, size, sort);
    }

    @Operation(summary = "Get All Network Entities by Customer Id")
    @GetMapping("/customer/{id}")
    public PagedResponse<NetworkEntity> getAllByOwnerId(@PathVariable Long id,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort,
            @RequestParam(name = "search", required = false, defaultValue = "") String search){
        return networkEntityService.getAllByOwnerId(id, page-1, size, sort, search);
    }

    @Operation(summary = "Get All Network Entities by Customer Id and Device Type")
    @GetMapping("/customer/{id}/device-type/{deviceType}")
    public PagedResponse<NetworkEntity> getAllByOwnerIdAndDeviceType(@PathVariable Long id, @PathVariable DeviceType deviceType,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort,
            @RequestParam(name = "search", required = false, defaultValue = "") String search){
        return networkEntityService.getAllByOwnerIdAndDeviceType(id, deviceType, page-1, size, sort, search);
    }

    @Operation(summary = "Get All Available Network Entities By Type")
    @GetMapping("/available/device-type/{deviceType}")
    public PagedResponse<NetworkEntity> getAllAvailableByDeviceType(@PathVariable DeviceType deviceType,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort,
            @RequestParam(name = "search", required = false, defaultValue = "") String search){
        return networkEntityService.getAllAvailableByDeviceType(page-1, size, sort, search, deviceType);
    }

    @Operation(summary = "Get Network Entity by Id")
    @GetMapping("/{id}")
    public ResponseEntity<NetworkEntity> getById(@PathVariable Long id){
        return networkEntityService.getById(id);
    }

    @Operation(summary = "Add Network Entity")
    @PostMapping
    public ResponseEntity<NetworkEntity> addNetworkEntity(@Valid @RequestBody NetworkEntityRequest networkEntityRequest){
        return networkEntityService.addNetworkEntity(networkEntityRequest);
    }

    @Operation(summary = "Update Network Entity")
    @PutMapping("/{id}")
    public ResponseEntity<NetworkEntity> updateNetworkEntity(@PathVariable Long id, @Valid @RequestBody NetworkEntityRequest networkEntityRequest){
        return networkEntityService.updateNetworkEntity(id, networkEntityRequest);
    }

    @Operation(summary = "Assign Network Entity To Customer")
    @PutMapping("/{id}/sell")
    public ResponseEntity<ApiResponse> assignNetworkEntity(@PathVariable Long id, @Valid @RequestBody NetworkEntitySellRequest networkEntitySellRequest){
        return networkEntityService.assignNetworkEntity(id, networkEntitySellRequest);
    }

    @Operation(summary = "Deactivate Network Entity")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse> deactivateNetworkEntity(@PathVariable Long id){
        return networkEntityService.deactivateNetworkEntity(id);
    }

    @Operation(summary = "Activate Network Entity")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse> activateNetworkEntity(@PathVariable Long id){
        return networkEntityService.activateNetworkEntity(id);
    }

    @Operation(summary = "Delete Network Entity")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteNetworkEntity(@PathVariable Long id){
        return networkEntityService.deleteNetworkEntity(id);
    }
}
