package com.example.CRM.service;

import com.example.CRM.model.Plan;
import com.example.CRM.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    @Autowired
    PlanRepository planRepository;

    public List<Plan> getAll() {
        return planRepository.findAll();
    }

    public Plan getById(Long id) {
        return planRepository.findById(id).orElse(null);
    }
}
