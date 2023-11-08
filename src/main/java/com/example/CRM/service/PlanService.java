package com.example.CRM.service;

import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.Plan;
import com.example.CRM.model.Subscription;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.repository.PlanRepository;
import com.example.CRM.repository.SubscriptionRepository;
import com.example.CRM.utils.AppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    final
    PlanRepository planRepository;

    final
    SubscriptionRepository subscriptionRepository;

    public PlanService(PlanRepository planRepository, SubscriptionRepository subscriptionRepository) {
        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public PagedResponse<Plan> getAll(int page, int size, String sort) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);

        Page<Plan> plans = planRepository.findAll(pageable);

        if(plans.getNumberOfElements() == 0){
            return new PagedResponse<>(List.of(), plans.getNumber(), plans.getSize(),
                    plans.getTotalElements(), plans.getTotalPages(), plans.isLast());
        }

        List<Plan> plansList = plans.getContent();

        return new PagedResponse<>(plansList, plans.getNumber(), plans.getSize(), plans.getTotalElements(),
                plans.getTotalPages(), plans.isLast());
    }

    public ResponseEntity<Plan> getById(Long id) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }

    // TODO: what kind of data validation?
    public ResponseEntity<Plan> addPlan(Plan plan) {
        return new ResponseEntity<>(planRepository.save(plan), HttpStatus.CREATED);
    }

    public ResponseEntity<Plan> updatePlan(Long id, Plan plan) {
        Plan existingPlan = planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));

        existingPlan.setName(plan.getName());
        existingPlan.setPrice(plan.getPrice());
        existingPlan.setPackageType(plan.getPackageType());
        existingPlan.setAmount(plan.getAmount());
        existingPlan.setDuration(plan.getDuration());
        existingPlan.setDescription(plan.getDescription());
        return new ResponseEntity<>(planRepository.save(existingPlan), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deletePlan(Long id) {
        planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));

        List<Subscription> subscriptions = subscriptionRepository.findAllByPlanId(id);
        subscriptionRepository.deleteAll(subscriptions);

        planRepository.deleteById(id);
        return new ResponseEntity<>(new ApiResponse(true, "Plan deleted successfully"), HttpStatus.OK);
    }
}
