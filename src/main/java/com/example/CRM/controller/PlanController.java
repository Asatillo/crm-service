package com.example.CRM.controller;

import com.example.CRM.model.Plan;
import com.example.CRM.service.PlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class PlanController {
    final
    PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping("/plans")
    public ResponseEntity<List<Plan>> getAll(){
        return planService.getAll();
    }

    @GetMapping("/plan/{id}")
    public ResponseEntity<Plan> getById(@PathVariable Long id){
        return planService.getById(id);
    }
}
