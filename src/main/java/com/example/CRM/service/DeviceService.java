package com.example.CRM.service;

import com.example.CRM.exceptions.InvalidInputException;
import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.feign.SalesInterface;
import com.example.CRM.model.Customer;
import com.example.CRM.model.Device;
import com.example.CRM.model.enums.DeviceType;
import com.example.CRM.model.template.DeviceTemplate;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.request.DeviceRequest;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.payload.request.DeviceSellRequest;
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
import java.util.List;
import java.util.HashMap;

@Service
public class DeviceService {

    DeviceRepository deviceRepository;
    DeviceTemplateRepository deviceTemplateRepository;
    CustomerRepository customerRepository;
    SubscriptionRepository subscriptionRepository;
    SalesInterface salesInterface;

    public DeviceService(DeviceRepository deviceRepository, DeviceTemplateRepository deviceTemplateRepository,
                         CustomerRepository customerRepository, SubscriptionRepository subscriptionRepository,
                         SalesInterface salesInterface) {
        this.deviceRepository = deviceRepository;
        this.deviceTemplateRepository = deviceTemplateRepository;
        this.customerRepository = customerRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.salesInterface = salesInterface;
    }

    public PagedResponse<Device> getAllDevices(int page, int size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, Device.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Device> devices = deviceRepository.findAll(pageable);
        PagedResponse<Device> pagedResponse = new PagedResponse<>(devices);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public ResponseEntity<Device> getById(Long id) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Device", "id", id));
        return new ResponseEntity<>(device, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> addDevices(@NonNull DeviceRequest deviceRequest) {
        DeviceTemplate deviceTemplate = deviceTemplateRepository.findById(deviceRequest.getDeviceTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("Device Template", "id", deviceRequest.getDeviceTemplateId()));

        for(int i = 0; i<deviceRequest.getAmount(); i++){
            deviceRepository.save(new Device(deviceTemplate));
        }
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Successfully created %d %s %s devices", deviceRequest.getAmount(),
                        deviceTemplate.getBrand(), deviceTemplate.getModel())), HttpStatus.OK);
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
        PagedResponse<Device> pagedResponse = new PagedResponse<>(devices);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public ResponseEntity<ApiResponse> deleteDevice(Long id) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Device", "id", id));
        deviceRepository.delete(device);
        return new ResponseEntity<>(new ApiResponse(true, "Device deleted successfully"), HttpStatus.OK);
    }

    public PagedResponse<Device> getByCustomerId(Long id, int page, int size, String sort, String search) {
        customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        AppUtils.validatePaginationRequestParams(page, size, sort, Device.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Device> devices = deviceRepository.findAllDevicesByOwner_Id(id, search, pageable);
        PagedResponse<Device> pagedResponse = new PagedResponse<>(devices);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public PagedResponse<Device> getByDeviceType(DeviceType deviceType, int page, Integer size, String sort, String search) {
        AppUtils.validatePaginationRequestParams(page, size, sort, Device.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Device> devices = deviceRepository.searchDevicesByDeviceType(pageable, deviceType, search);
        PagedResponse<Device> pagedResponse = new PagedResponse<>(devices);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public ResponseEntity<Device> sellDevice(Long id, DeviceSellRequest customerRequest, String authHeader) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Device", "id", id));
        Customer customer = customerRepository.findById(customerRequest.getCustomerId()).orElseThrow(() ->
                new ResourceNotFoundException("Customer", "id", customerRequest.getCustomerId()));

        device.setOwner(customer);
        device.setPurchaseDate(AppUtils.getCurrentTime());

        if(!device.getDeviceTemplate().isRouter()){
            HashMap<String, Object> sale = getSale(device, customer, customerRequest.getPromotionId());

            ResponseEntity<HashMap> saleResponse = salesInterface.add(authHeader, sale);
            if(!saleResponse.getStatusCode().equals(HttpStatus.CREATED)){
                throw new InvalidInputException(new ApiResponse(false, "Sale creation failed"));
            }
        }

        return new ResponseEntity<>(deviceRepository.save(device), HttpStatus.OK);
    }

    private static HashMap<String, Object> getSale(Device device, Customer customer, Long promotionId) {
        HashMap<String, Object> sale = new HashMap<>();
        if(promotionId != null){
            sale.put("promotionId", promotionId);
        }
        sale.put("description", String.format("Device %s sold to %s", device.getDeviceTemplate().getModel(),
                customer.getFullName()));
        sale.put("productId", device.getId());
        sale.put("productType", "DEVICE");
        sale.put("customerId", customer.getId());
        sale.put("customer", customer.getFullName());
        sale.put("amount", device.getDeviceTemplate().getPrice());
        return sale;
    }

    public PagedResponse<Device> getAvailableDevices(int page, Integer size, String sort, String search, DeviceType type) {
        AppUtils.validatePaginationRequestParams(page, size, sort, Device.class);

        if(size == -1){
            List<Device> allDevices = deviceRepository.findAllAvailableDevices(search, type);
            return new PagedResponse<>(allDevices, 0, allDevices.size(), allDevices.size(), 1);
        }else{
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
            Page<Device> devices = deviceRepository.findAllAvailableDevices(search, type, pageable);
            PagedResponse<Device> pagedResponse = new PagedResponse<>(devices);

            AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

            return pagedResponse;
        }
    }
}
