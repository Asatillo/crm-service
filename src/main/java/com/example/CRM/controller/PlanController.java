package com.example.CRM.controller;

import com.example.CRM.model.Plan;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.payload.request.PlanRequest;
import com.example.CRM.service.PlanService;
import com.example.CRM.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("plans")
public class PlanController {
    final
    PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    // TODO: add filters, sortorder
    @Operation(summary = "Get All Plans")
    @GetMapping
    public PagedResponse<Plan> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort
    ){
        return planService.getAll(page-1, size, sort);
    }

    @Operation(summary = "Get Plan by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Plan> getById(@PathVariable Long id){
        return planService.getById(id);
    }

    @Operation(summary = "Add Plan")
    @PostMapping
    public ResponseEntity<Plan> addPlan(@Valid @RequestBody PlanRequest planRequest){
        return planService.addPlan(planRequest);
    }

    @Operation(summary = "Update Plan")
    @PutMapping("/{id}")
    public ResponseEntity<Plan> updatePlan(@PathVariable Long id, @Valid @RequestBody PlanRequest planRequest){
        return planService.updatePlan(id, planRequest);
    }

    @Operation(summary = "Get Plans by Designated Device Type")
    @GetMapping("/device-type/{deviceType}")
    public PagedResponse<Plan> getPlansByDesignatedDeviceType(@PathVariable String deviceType,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort){
        return planService.getPlansByDesignatedDeviceType(deviceType, page-1, size, sort);
    }

    @Operation(summary = "Deactivate Plan")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse> deactivatePlan(@PathVariable Long id){
        return planService.deactivatePlan(id);
    }

    @Operation(summary = "Activate Plan")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse> activatePlan(@PathVariable Long id){
        return planService.activatePlan(id);
    }

    @Operation(summary = "Delete Plan")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePlan(@PathVariable Long id){
        return planService.deletePlan(id);
    }
}
