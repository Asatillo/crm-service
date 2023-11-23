package com.example.CRM.service;

import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.Customer;
import com.example.CRM.model.Device;
import com.example.CRM.model.template.DeviceTemplate;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.DeviceRequest;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.repository.CustomerRepository;
import com.example.CRM.repository.DeviceRepository;
import com.example.CRM.repository.DeviceTemplateRepository;
import com.example.CRM.utils.AppUtils;
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

    public DeviceService(DeviceRepository deviceRepository, DeviceTemplateRepository deviceTemplateRepository, CustomerRepository customerRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceTemplateRepository = deviceTemplateRepository;
        this.customerRepository = customerRepository;
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

    public ResponseEntity<Device> addDevice(DeviceRequest deviceRequest) {
        DeviceTemplate deviceTemplate = deviceTemplateRepository.findById(deviceRequest.getDeviceTemplateId()).orElseThrow(() -> new ResourceNotFoundException("Device Template", "id", deviceRequest.getDeviceTemplateId()));
        Customer customer = customerRepository.findById(deviceRequest.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", deviceRequest.getCustomerId()));

        Device device = new Device(deviceTemplate, customer, LocalDateTime.now());
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
        Page<Device> devices = deviceRepository.findByOwner_Id(id, pageable);
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
