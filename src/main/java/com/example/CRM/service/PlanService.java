package com.example.CRM.service;

import com.example.CRM.exceptions.InvalidInputException;
import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.Plan;
import com.example.CRM.model.Service;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.payload.request.PlanRequest;
import com.example.CRM.repository.PlanRepository;
import com.example.CRM.repository.ServiceRepository;
import com.example.CRM.repository.SubscriptionRepository;
import com.example.CRM.utils.AppUtils;
import lombok.NonNull;
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
        PagedResponse<Plan> pagedResponse = new PagedResponse<>(plans);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public ResponseEntity<Plan> getById(Long id) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }

    public PagedResponse<Plan> getPlansByDesignatedDeviceType(String deviceType, int page, Integer size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, Plan.class);
        AppUtils.validateDeviceType(deviceType);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Plan> plans = planRepository.findAllByDesignatedDeviceType(deviceType, pageable);
        PagedResponse<Plan> pagedResponse = new PagedResponse<>(plans);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public ResponseEntity<Plan> addPlan(@NonNull PlanRequest planRequest) {
        List<Service> services = validateServiceIds(planRequest.getServices(), planRequest.getDesignatedDeviceType());

        Plan plan = new Plan(planRequest.getName(), planRequest.getDuration(), planRequest.getDescription(),
                planRequest.getPrice(), services, planRequest.getDesignatedDeviceType());

        return new ResponseEntity<>(planRepository.save(plan), HttpStatus.CREATED);
    }

    public ResponseEntity<Plan> updatePlan(Long id, @NonNull PlanRequest planRequest) {
        Plan existingPlan = planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));

        existingPlan.setServices(validateServiceIds(planRequest.getServices(), planRequest.getDesignatedDeviceType()));

        if(!existingPlan.getName().equals(planRequest.getName())){
            existingPlan.setName(planRequest.getName());
        }

        if(!existingPlan.getDescription().equals(planRequest.getDescription())){
            existingPlan.setDescription(planRequest.getDescription());
        }

        if(!existingPlan.getPrice().equals(planRequest.getPrice())){
            existingPlan.setPrice(planRequest.getPrice());
        }

        if(!existingPlan.getDuration().equals(planRequest.getDuration())){
            existingPlan.setDuration(planRequest.getDuration());
        }

        if(!existingPlan.getDesignatedDeviceType().equals(planRequest.getDesignatedDeviceType())){
            existingPlan.setDesignatedDeviceType(planRequest.getDesignatedDeviceType());
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

    public ResponseEntity<Plan> changePlanActive(Long id, boolean active) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan", "id", id));
        plan.setActive(active);
        return new ResponseEntity<>(planRepository.save(plan), HttpStatus.OK);
    }

    private List<Service> convertServiceIdsToService(@NonNull List<Long> serviceIds){
        List<Service> services = new ArrayList<>();
        for(Long serviceId: serviceIds){
            Service service = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Service", "id", serviceId));
            services.add(service);
        }
        return services;
    }

    private void validateServiceDeviceTypes(@NonNull List<Service> services, String deviceType){
        for(Service service: services){
            if(!service.getDesignatedDeviceType().equals(deviceType)){
                throw new InvalidInputException(new ApiResponse(false,
                        String.format("Service '%s' is not compatible with %s device type",
                                service.getName(), deviceType)));
            }
        }
    }

    private @NonNull List<Service> validateServiceIds(List<Long> serviceIds, String deviceType){
        List<Service> services = convertServiceIdsToService(serviceIds);
        validateServiceDeviceTypes(services, deviceType);

        return services;
    }
}
