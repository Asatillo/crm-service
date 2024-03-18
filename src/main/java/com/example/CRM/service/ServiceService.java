package com.example.CRM.service;

import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.enums.DeviceType;
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
    public PagedResponse<Service> getAll(int page, int size, String sort, String search) {
        AppUtils.validatePaginationRequestParams(page, size, sort, com.example.CRM.model.Service.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Service> services = serviceRepository.findAllWithSearch(search, pageable);
        PagedResponse<Service> pagedResponse = new PagedResponse<>(services);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public ResponseEntity<com.example.CRM.model.Service> getById(Long id) {
        Service service = serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));
        return new ResponseEntity<>(service, HttpStatus.OK);
    }

    public PagedResponse<Service> getServicesByDesignatedDeviceType(DeviceType deviceType, Integer page, Integer size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, com.example.CRM.model.Service.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Service> services = serviceRepository.findByDesignatedDeviceType(deviceType, pageable);
        PagedResponse<Service> pagedResponse = new PagedResponse<>(services);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public ResponseEntity<com.example.CRM.model.Service> addService(Service service) {
        return new ResponseEntity<>(serviceRepository.save(service), HttpStatus.CREATED);
    }

    public ResponseEntity<com.example.CRM.model.Service> updateService(Long id, com.example.CRM.model.@NonNull Service service) {
        Service existingService = serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));

        if(!existingService.getName().equals(service.getName())){
            existingService.setName(service.getName());
        }

        if(!existingService.getDesignatedDeviceType().equals(service.getDesignatedDeviceType())){
            existingService.setDesignatedDeviceType(service.getDesignatedDeviceType());
        }

        if(!existingService.getType().equals(service.getType())){
            existingService.setType(service.getType());
        }

        if(existingService.getAmount() != service.getAmount()){
            existingService.setAmount(service.getAmount());
        }

        if(!existingService.getPrice().equals(service.getPrice())){
            existingService.setPrice(service.getPrice());
        }

        if(existingService.isActive() != service.isActive()){
            existingService.setActive(service.isActive());
        }

        return new ResponseEntity<>(serviceRepository.save(existingService), HttpStatus.OK);
    }

    public ResponseEntity<Service> changeActive(Long id, boolean active) {
        Service service = serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));

        service.setActive(active);
        return new ResponseEntity<>(serviceRepository.save(service), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deleteService(Long id) {
        serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));

        // check if there are subscription with this service
        // if there are, throw an exception

        serviceRepository.deleteById(id);
        return new ResponseEntity<>(new ApiResponse(true, "Service deleted successfully"), HttpStatus.OK);
    }
}
