package com.example.CRM.service;

import com.example.CRM.exceptions.ReferencedRecordException;
import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.enums.DeviceType;
import com.example.CRM.model.template.DeviceTemplate;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.repository.DeviceRepository;
import com.example.CRM.repository.DeviceTemplateRepository;
import com.example.CRM.utils.AppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

@Service
public class DeviceTemplateService {

    final DeviceTemplateRepository deviceTemplateRepository;
    final DeviceRepository deviceRepository;

    public DeviceTemplateService(DeviceTemplateRepository deviceTemplateRepository, DeviceRepository deviceRepository) {
        this.deviceTemplateRepository = deviceTemplateRepository;
        this.deviceRepository = deviceRepository;
    }
    public PagedResponse<DeviceTemplate> getAllDeviceTemplates(int page, int size, String sort, Boolean paginate) {
        Pageable pageable;
        PagedResponse<DeviceTemplate> pagedResponse;
        if(paginate){
            AppUtils.validatePaginationRequestParams(page, size, sort, DeviceTemplate.class);
            pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
            Page<DeviceTemplate> deviceTemplates = deviceTemplateRepository.findAll(pageable);
            pagedResponse = new PagedResponse<>(deviceTemplates);
            AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());
        } else {
            AppUtils.validateSortFieldExists(sort, DeviceTemplate.class);
            pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, sort);
            Page<DeviceTemplate> deviceTemplates = deviceTemplateRepository.findAll(pageable);
            pagedResponse = new PagedResponse<>(deviceTemplates.getContent(), 1, -1,
                    deviceTemplates.getTotalElements(), 1);
        }

        return pagedResponse;
    }

    public ResponseEntity<DeviceTemplate> getById(Long id) {
        DeviceTemplate deviceTemplate = deviceTemplateRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Device Template", "id", id));
        return new ResponseEntity<>(deviceTemplate, HttpStatus.OK);
    }

    public PagedResponse<DeviceTemplate> getByBrand(String brand, int page, int size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, DeviceTemplate.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<DeviceTemplate> deviceTemplates = deviceTemplateRepository.findByBrand(brand, pageable);
        PagedResponse<DeviceTemplate> pagedResponse = new PagedResponse<>(deviceTemplates);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public PagedResponse<DeviceTemplate> getByDeviceType(DeviceType deviceType, int page, int size, String sort, String search) {
        AppUtils.validatePaginationRequestParams(page, size, sort, DeviceTemplate.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<DeviceTemplate> deviceTemplates = deviceTemplateRepository.searchByDeviceType(pageable, deviceType, search);
        PagedResponse<DeviceTemplate> pagedResponse = new PagedResponse<>(deviceTemplates);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public ResponseEntity<DeviceTemplate> createDeviceTemplate(DeviceTemplate deviceTemplate) {
        return new ResponseEntity<>(deviceTemplateRepository.save(deviceTemplate), HttpStatus.CREATED);
    }

    public ResponseEntity<DeviceTemplate> updateDeviceTemplate(Long id, DeviceTemplate deviceTemplate) {
        DeviceTemplate existingDeviceTemplate = deviceTemplateRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Device Template", "id", id));

        if(!deviceTemplate.getBrand().equals(existingDeviceTemplate.getBrand())){
            existingDeviceTemplate.setBrand(deviceTemplate.getBrand());
        }
        if(!deviceTemplate.getModel().equals(existingDeviceTemplate.getModel())){
            existingDeviceTemplate.setModel(deviceTemplate.getModel());
        }
        if(deviceTemplate.getDeviceType().equals(existingDeviceTemplate.getDeviceType())){
            existingDeviceTemplate.setDeviceType(deviceTemplate.getDeviceType());
        }

        if(!deviceTemplate.getStorage().equals(existingDeviceTemplate.getStorage())){
            existingDeviceTemplate.setStorage(deviceTemplate.getStorage());
        }

        if(!deviceTemplate.getWarrantyDuration().equals(existingDeviceTemplate.getWarrantyDuration())){
            existingDeviceTemplate.setWarrantyDuration(deviceTemplate.getWarrantyDuration());
        }
        return new ResponseEntity<>(deviceTemplateRepository.save(existingDeviceTemplate), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deleteDeviceTemplate(Long id) {
        DeviceTemplate deviceTemplate = deviceTemplateRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Device Template", "id", id));

        if(!deviceRepository.findByDeviceTemplate(deviceTemplate).isEmpty()){
            throw new ReferencedRecordException("Device Template", deviceTemplate.getId(), "Device");
        }

        deviceTemplateRepository.delete(deviceTemplate);
        return new ResponseEntity<>(new ApiResponse(true, "Device Template deleted successfully"), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deactivateDeviceTemplate(Long id) {
        DeviceTemplate deviceTemplate = deviceTemplateRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Device Template", "id", id));

        if(!deviceTemplate.isActive()){
            return new ResponseEntity<>(new ApiResponse(false, "Device Template was already deactivated"), HttpStatus.OK);
        }

        deviceTemplate.setActive(false);
        deviceTemplateRepository.save(deviceTemplate);

        return new ResponseEntity<>(new ApiResponse(true, "Device Template deactivated successfully"), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> activateDeviceTemplate(Long id) {
        DeviceTemplate deviceTemplate = deviceTemplateRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Device Template", "id", id));

        if(deviceTemplate.isActive()){
            return new ResponseEntity<>(new ApiResponse(false, "Device Template was already activated"), HttpStatus.OK);
        }

        deviceTemplate.setActive(true);
        deviceTemplateRepository.save(deviceTemplate);

        return new ResponseEntity<>(new ApiResponse(true, "Device Template activated successfully"), HttpStatus.OK);
    }
}
