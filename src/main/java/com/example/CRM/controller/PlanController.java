package com.example.CRM.controller;

import com.example.CRM.model.Plan;
import com.example.CRM.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class PlanController {
    final
    PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    // TODO: add filters, perpage, page, sortby, sortorder
    @Operation(summary = "Get All Plans")
    @GetMapping("/plans")
    public ResponseEntity<List<Plan>> getAll(){
        return planService.getAll();
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

    @Operation(summary = "Delete Plan")
    @DeleteMapping("/plan/{id}")
    public ResponseEntity<Long> deletePlan(@PathVariable Long id){
        return planService.deletePlan(id);
    }
}
