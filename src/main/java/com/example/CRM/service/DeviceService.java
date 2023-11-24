package com.example.CRM.service;

import com.example.CRM.exceptions.InvalidInputException;
import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.Device;
import com.example.CRM.model.template.DeviceTemplate;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.request.DeviceRequest;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.repository.CustomerRepository;
import com.example.CRM.repository.DeviceRepository;
import com.example.CRM.repository.DeviceTemplateRepository;
import com.example.CRM.repository.SubscriptionRepository;
import com.example.CRM.utils.AppUtils;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeviceService {

    DeviceRepository deviceRepository;
    DeviceTemplateRepository deviceTemplateRepository;
    CustomerRepository customerRepository;
    SubscriptionRepository subscriptionRepository;

    public DeviceService(DeviceRepository deviceRepository, DeviceTemplateRepository deviceTemplateRepository,
                         CustomerRepository customerRepository, SubscriptionRepository subscriptionRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceTemplateRepository = deviceTemplateRepository;
        this.customerRepository = customerRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public PagedResponse<Device> getAllDevices(int page, int size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, Device.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Device> devices = deviceRepository.findAll(pageable);
        PagedResponse<Device> pagedResponse = new PagedResponse<>();

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());

        return pagedResponse.returnPagedResponse(devices);
    }

    public ResponseEntity<Device> getById(Long id) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Device", "id", id));
        return new ResponseEntity<>(device, HttpStatus.OK);
    }

    public ResponseEntity<Device> addDevice(@NonNull DeviceRequest deviceRequest) {
        DeviceTemplate deviceTemplate = deviceTemplateRepository.findById(deviceRequest.getDeviceTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("Device Template", "id", deviceRequest.getDeviceTemplateId()));

        Device device = new Device(deviceTemplate, deviceRequest.getPurchaseDate(), deviceRequest.getColor());
        return new ResponseEntity<>(deviceRepository.save(device), HttpStatus.OK);
    }

    public ResponseEntity<Device> updateDevice(Long id, @NonNull DeviceRequest deviceRequest) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Device", "id", id));

        if(!deviceRequest.getDeviceTemplateId().equals(device.getDeviceTemplate().getId())){
            DeviceTemplate deviceTemplate = deviceTemplateRepository.findById(deviceRequest.getDeviceTemplateId())
                    .orElseThrow(() -> new ResourceNotFoundException("Device Template", "id", deviceRequest.getDeviceTemplateId()));
            if(!deviceTemplate.getDeviceType().equals(device.getDeviceTemplate().getDeviceType())){
                throw new InvalidInputException(new ApiResponse(false, "Device type cannot be changed"));
            }
            device.setDeviceTemplate(deviceTemplate);
        }

        return new ResponseEntity<>(deviceRepository.save(device), HttpStatus.OK);
    }

    public PagedResponse<Device> getByDeviceTemplateId(Long id, int page, Integer size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, Device.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Device> devices = deviceRepository.findByDeviceTemplate_Id(id, pageable);
        PagedResponse<Device> pagedResponse = new PagedResponse<>();

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());

        return pagedResponse.returnPagedResponse(devices);
    }

    public ResponseEntity<ApiResponse> deleteDevice(Long id) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Device", "id", id));
        deviceRepository.delete(device);
        return new ResponseEntity<>(new ApiResponse(true, "Device deleted successfully"), HttpStatus.OK);
    }

    public PagedResponse<Device> getByCustomerId(Long id, int page, int size, String sort) {
        customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        AppUtils.validatePaginationRequestParams(page, size, sort, Device.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Device> devices = subscriptionRepository.findAllByDevice_Id(id, pageable);
        PagedResponse<Device> pagedResponse = new PagedResponse<>();

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());

        return pagedResponse.returnPagedResponse(devices);
    }

    public PagedResponse<Device> getByDeviceType(String deviceType, int page, Integer size, String sort) {
        AppUtils.validateDeviceType(deviceType);
        AppUtils.validatePaginationRequestParams(page, size, sort, Device.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Device> devices;
        devices = deviceRepository.findByDeviceTemplate_DeviceType(deviceType, pageable);
        PagedResponse<Device> pagedResponse = new PagedResponse<>();

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());

        return pagedResponse.returnPagedResponse(devices);
    }
}
