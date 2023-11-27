package com.example.CRM.service;

import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.repository.ServiceRepository;
import com.example.CRM.utils.AppUtils;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.CRM.model.Service;

@org.springframework.stereotype.Service
public class ServiceService {

    final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }
    public PagedResponse<Service> getAll(int page, int size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, com.example.CRM.model.Service.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Service> services = serviceRepository.findAll(pageable);
        PagedResponse<Service> pagedResponse = new PagedResponse<>(services.getContent(), services.getNumber(), services.getSize(),
                services.getTotalElements(), services.getTotalPages());

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());

        return pagedResponse.returnPagedResponse(services);
    }

    public ResponseEntity<com.example.CRM.model.Service> getById(Long id) {
        Service service = serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));
        return new ResponseEntity<>(service, HttpStatus.OK);
    }

    public PagedResponse<Service> getServicesByDesignatedDeviceType(String deviceType, Integer page, Integer size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, com.example.CRM.model.Service.class);
        AppUtils.validateDeviceType(deviceType);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Service> services = serviceRepository.findByDesignatedDeviceType(deviceType, pageable);
        PagedResponse<Service> pagedResponse = new PagedResponse<>(services.getContent(), services.getNumber(), services.getSize(),
                services.getTotalElements(), services.getTotalPages());

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());

        return pagedResponse.returnPagedResponse(services);
    }

    public ResponseEntity<com.example.CRM.model.Service> addService(Service service) {
        return new ResponseEntity<>(serviceRepository.save(service), HttpStatus.CREATED);
    }

    public ResponseEntity<com.example.CRM.model.Service> updateService(Long id, com.example.CRM.model.@NonNull Service service) {
        Service existingService = serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));

        if(!existingService.getName().equals(service.getName())){
            existingService.setName(service.getName());
        }

        if(!existingService.getType().equals(service.getType())){
            existingService.setType(service.getType());
        }

        if(existingService.getAmount() != service.getAmount()){
            existingService.setAmount(service.getAmount());
        }

        if(existingService.isActive() != service.isActive()){
            existingService.setActive(service.isActive());
        }

        return new ResponseEntity<>(serviceRepository.save(existingService), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deactivateService(Long id) {
        Service service = serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));

        if(!service.isActive()){
            return new ResponseEntity<>(new ApiResponse(false, "Service is already deactivated"), HttpStatus.BAD_REQUEST);
        }
        service.setActive(false);
        serviceRepository.save(service);
        return new ResponseEntity<>(new ApiResponse(true, "Service deactivated successfully"), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> activateService(Long id) {
        Service service = serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));

        if(service.isActive()){
            return new ResponseEntity<>(new ApiResponse(false, "Service is already activated"), HttpStatus.BAD_REQUEST);
        }
        service.setActive(true);
        serviceRepository.save(service);
        return new ResponseEntity<>(new ApiResponse(true, "Service activated successfully"), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deleteService(Long id) {
        serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));

        // check if there are subscription with this service
        // if there are, throw an exception

        serviceRepository.deleteById(id);
        return new ResponseEntity<>(new ApiResponse(true, "Service deleted successfully"), HttpStatus.OK);
    }
}
