package com.example.CRM.controller;

import com.example.CRM.model.NetworkEntity;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.request.NetworkEntityRequest;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.service.NetworkEntityService;
import com.example.CRM.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class NetworkEntityController {

    final NetworkEntityService networkEntityService;

    public NetworkEntityController(NetworkEntityService networkEntityService) {
        this.networkEntityService = networkEntityService;
    }

    @Operation(summary = "Get All Network Entities")
    @GetMapping("/network-entities")
    public PagedResponse<NetworkEntity> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort
    ){
        return networkEntityService.getAll(page-1, size, sort);
    }

    @Operation(summary = "Get All Router Network Entities")
    @GetMapping("/network-entities/device-type/{deviceType}")
    public PagedResponse<NetworkEntity> getAllRouters(@PathVariable String deviceType,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort
    ){
        return networkEntityService.getAllByDeviceType(deviceType, page-1, size, sort);
    }

    @Operation(summary = "Get All Network Entities by Owner Id")
    @GetMapping("/network-entities/owner/{id}")
    public PagedResponse<NetworkEntity> getAllByOwnerId(@PathVariable Long id,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return networkEntityService.getAllByOwnerId(id, page-1, size, sort);
    }

    @Operation(summary = "Get Network Entity by Id")
    @GetMapping("/network-entities/{id}")
    public ResponseEntity<NetworkEntity> getById(@PathVariable Long id){
        return networkEntityService.getById(id);
    }

    @Operation(summary = "Add Network Entity")
    @PostMapping("/network-entities")
    public ResponseEntity<NetworkEntity> addNetworkEntity(@Valid @RequestBody NetworkEntityRequest networkEntityRequest){
        return networkEntityService.addNetworkEntity(networkEntityRequest);
    }

    @Operation(summary = "Update Network Entity")
    @PutMapping("/network-entities/{id}")
    public ResponseEntity<NetworkEntity> updateNetworkEntity(@PathVariable Long id, @Valid @RequestBody NetworkEntityRequest networkEntityRequest){
        return networkEntityService.updateNetworkEntity(id, networkEntityRequest);
    }

    @Operation(summary = "Deactivate Network Entity")
    @PatchMapping("/network-entities/{id}/deactivate")
    public ResponseEntity<ApiResponse> deactivateNetworkEntity(@PathVariable Long id){
        return networkEntityService.deactivateNetworkEntity(id);
    }

    @Operation(summary = "Activate Network Entity")
    @PatchMapping("/network-entities/{id}/activate")
    public ResponseEntity<ApiResponse> activateNetworkEntity(@PathVariable Long id){
        return networkEntityService.activateNetworkEntity(id);
    }

    @Operation(summary = "Delete Network Entity")
    @DeleteMapping("/network-entities/{id}")
    public ResponseEntity<ApiResponse> deleteNetworkEntity(@PathVariable Long id){
        return networkEntityService.deleteNetworkEntity(id);
    }
}
