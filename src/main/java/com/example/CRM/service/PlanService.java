package com.example.CRM.service;

import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.Plan;
import com.example.CRM.model.Service;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.payload.PlanRequest;
import com.example.CRM.repository.PlanRepository;
import com.example.CRM.repository.ServiceRepository;
import com.example.CRM.repository.SubscriptionRepository;
import com.example.CRM.utils.AppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class PlanService {

    final PlanRepository planRepository;

    final SubscriptionRepository subscriptionRepository;

    final ServiceRepository serviceRepository;

    public PlanService(PlanRepository planRepository, SubscriptionRepository subscriptionRepository, ServiceRepository serviceRepository) {
        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.serviceRepository = serviceRepository;
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

    public ResponseEntity<Plan> addPlan(PlanRequest planRequest) {
        List<Service> services = convertServiceIdsToService(planRequest.getServices());
        Plan plan = new Plan(planRequest.getName(), planRequest.getDuration(), planRequest.getDescription(),
                planRequest.getPrice(), services);

        return new ResponseEntity<>(planRepository.save(plan), HttpStatus.CREATED);
    }

    public ResponseEntity<Plan> updatePlan(Long id, PlanRequest planRequest) {
        Plan existingPlan = planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));

        if(!existingPlan.getName().equals(planRequest.getName())){
            existingPlan.setName(planRequest.getName());
        }

        if(!existingPlan.getDescription().equals(planRequest.getDescription())){
            existingPlan.setDescription(planRequest.getDescription());
        }

        if(existingPlan.getPrice() != planRequest.getPrice()){
            existingPlan.setPrice(planRequest.getPrice());
        }

        if(existingPlan.getDuration().equals(planRequest.getDuration())){
            existingPlan.setDuration(planRequest.getDuration());
        }

        if(!existingPlan.getServices().equals(planRequest.getServices())){
            existingPlan.setServices(convertServiceIdsToService(planRequest.getServices()));
        }
        return new ResponseEntity<>(planRepository.save(existingPlan), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deletePlan(Long id) {
        planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));

        if(subscriptionRepository.existsByPlan_Id(id)){
            return new ResponseEntity<>(new ApiResponse(false, "Plan cannot be deleted as it is associated with a subscription"), HttpStatus.OK);
        }

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

    private List<Service> convertServiceIdsToService(List<Long> serviceIds){
        List<Service> services = new ArrayList<>();
        for(Long serviceId: serviceIds){
            Service service = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId));
            services.add(service);
        }
        return services;
    }
}
