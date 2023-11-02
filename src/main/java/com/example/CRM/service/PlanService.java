package com.example.CRM.service;

import com.example.CRM.model.Plan;
import com.example.CRM.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    @Autowired
    PlanRepository planRepository;

    public ResponseEntity<List<Plan>> getAll() {
        return new ResponseEntity<>(planRepository.findAll(), HttpStatus.OK);
    }

    // TODO: data validation
    public ResponseEntity<Plan> getById(Long id) {
        return new ResponseEntity<>(planRepository.findById(id).orElse(null), HttpStatus.OK);
    }

    // TODO: data validation
    public ResponseEntity<Plan> addPlan(Plan plan) {
        return new ResponseEntity<>(planRepository.save(plan), HttpStatus.CREATED);
    }

    // TODO: data validation
    public ResponseEntity<Plan> updatePlan(Long id, Plan plan) {
        return new ResponseEntity<>(planRepository.save(plan), HttpStatus.OK);
    }

    // TODO: data validation
    public ResponseEntity<Long> deletePlan(Long id) {
        planRepository.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
