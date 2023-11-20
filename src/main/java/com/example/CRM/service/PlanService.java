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

    final PlanRepository planRepository;

    final SubscriptionRepository subscriptionRepository;

    public PlanService(PlanRepository planRepository, SubscriptionRepository subscriptionRepository) {
        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public PagedResponse<Plan> getAll(int page, int size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, Plan.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Plan> plans = planRepository.findAll(pageable);
        PagedResponse<Plan> pagedResponse = new PagedResponse<>();

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());

        return pagedResponse.returnPagedResponse(plans);
    }

    public ResponseEntity<Plan> getById(Long id) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }

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

    public ResponseEntity<ApiResponse> deactivatePlan(Long id) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));
        if(!plan.isActive()){
            return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, "Plan already deactivated"), HttpStatus.OK);
        }
        plan.setActive(false);
        planRepository.save(plan);
        return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Plan deactivated successfully"), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> activatePlan(Long id) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));
        if(plan.isActive()){
            return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, "Plan already activated"), HttpStatus.OK);
        }
        plan.setActive(true);
        planRepository.save(plan);
        return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Plan activated successfully"), HttpStatus.OK);
    }
}
