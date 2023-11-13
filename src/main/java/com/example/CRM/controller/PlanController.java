package com.example.CRM.controller;

import com.example.CRM.model.Plan;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.service.PlanService;
import com.example.CRM.utils.AppConstants;
import com.example.CRM.utils.AppUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class PlanController {
    final
    PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    // TODO: add filters, sortorder
    @Operation(summary = "Get All Plans")
    @GetMapping("/plans")
    public PagedResponse<Plan> getAll(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = AppConstants.DEFAULT_SORT_PROPERTY) String sort
    ){
        AppUtils.validatePageNumberAndSize(page, size, sort);
        return planService.getAll(page, size, sort);
    }

    @Operation(summary = "Get Plan by Id")
    @GetMapping("/plan/{id}")
    public ResponseEntity<Plan> getById(@PathVariable Long id){
        return planService.getById(id);
    }

    @Operation(summary = "Add Plan")
    @PostMapping("/plan")
    public ResponseEntity<Plan> addPlan(@RequestBody Plan plan){
        return planService.addPlan(plan);
    }

    @Operation(summary = "Update Plan")
    @PutMapping("/plan/{id}")
    public ResponseEntity<Plan> updatePlan(@PathVariable Long id, @RequestBody Plan plan){
        return planService.updatePlan(id, plan);
    }

    @Operation(summary = "Deactivate Plan")
    @PatchMapping("/plan/{id}/deactivate")
    public ResponseEntity<ApiResponse> deactivatePlan(@PathVariable Long id){
        return planService.deactivatePlan(id);
    }

    @Operation(summary = "Delete Plan")
    @DeleteMapping("/plan/{id}")
    public ResponseEntity<ApiResponse> deletePlan(@PathVariable Long id){
        return planService.deletePlan(id);
    }
}
