package com.example.CRM.service;

import com.example.CRM.exceptions.ApiException;
import com.example.CRM.exceptions.InvalidInputException;
import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.feign.SalesInterface;
import com.example.CRM.model.Customer;
import com.example.CRM.model.Device;
import com.example.CRM.model.enums.DeviceType;
import com.example.CRM.model.template.DeviceTemplate;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.payload.request.DeviceRequest;
import com.example.CRM.repository.CustomerRepository;
import com.example.CRM.repository.DeviceRepository;
import com.example.CRM.repository.DeviceTemplateRepository;
import com.example.CRM.repository.SubscriptionRepository;
import com.example.CRM.service.DeviceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static com.example.CRM.model.enums.SegmentTypes.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DevicesTest {
    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private DeviceTemplateRepository deviceTemplateRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private SalesInterface salesInterface;

    @InjectMocks
    private DeviceService deviceService;

    @Test
    public void DeviceService_GetAllDevices_ReturnsAllDevices() {
        DeviceTemplate deviceMobileTemplate1 = new DeviceTemplate("Galaxy S20", "Samsung", DeviceType.MOBILE, Period.ZERO.toString(), 300000.0, "white", 128);
        DeviceTemplate deviceMobileTemplate2 = new DeviceTemplate("Galaxy S10", "Samsung", DeviceType.MOBILE, Period.ofYears(1).toString(), 200000.0, "black", 128);
        DeviceTemplate deviceMobileTemplate3 = new DeviceTemplate("Iphone XS", "Apple", DeviceType.MOBILE, Period.ofYears(1).toString(), 400000.0, "silver", 256);

        Customer customer1 = new Customer("Alice", "Smith", "alice@example.com", "123 Main St", "Budapest", LocalDate.of(1990, 5, 15), PREMIUM);
        Customer customer2 = new Customer("Bob", "Johnson", "bob@example.com", "456 Elm St", "Debrecen", LocalDate.of(1985, 3, 10), GOLD);
        Customer customer3 = new Customer("Eva", "Andersen", "eva@example.com", "789 Oak St", "Szeged", LocalDate.of(1988, 8, 20), SILVER);

        Device deviceMobile1 = new Device(deviceMobileTemplate1, LocalDateTime.now().minus(Period.ofMonths(1)), customer1);
        Device deviceMobile2 = new Device(deviceMobileTemplate2, LocalDateTime.now().minus(Period.ofMonths(2)), customer2);
        Device deviceMobile3 = new Device(deviceMobileTemplate3, LocalDateTime.now().minus(Period.ofMonths(3)), customer3);

        Page<Device> devicePage = new PageImpl<>(List.of(deviceMobile1, deviceMobile2, deviceMobile3));
        Mockito.when(deviceRepository.findAll(Mockito.any(Pageable.class))).thenReturn(devicePage);

        PagedResponse<Device> pagedResponse = deviceService.getAllDevices(0, 3, "id");
        assertNotNull(pagedResponse);
        assertEquals(pagedResponse.getContent().size(), 3);
        assertEquals(pagedResponse.getContent().get(0).getPurchaseDate(), deviceMobile1.getPurchaseDate());
    }

    @Test
    public void DeviceService_GetDeviceById_ReturnsDevice() {
        DeviceTemplate deviceMobileTemplate = new DeviceTemplate("Galaxy S20", "Samsung", DeviceType.MOBILE, Period.ZERO.toString(), 300000.0, "white", 128);
        Customer customer1 = new Customer("Alice", "Smith", "alice@example.com", "123 Main St", "Budapest", LocalDate.of(1990, 5, 15), PREMIUM);
        Device deviceMobile = new Device(deviceMobileTemplate, LocalDateTime.now().minus(Period.ofMonths(1)), customer1);
        Mockito.when(deviceRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(deviceMobile));

        ResponseEntity<Device> response = deviceService.getById(1L);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void DeviceService_CreateDevice_ReturnsDevice() {
        DeviceTemplate deviceMobileTemplate = new DeviceTemplate("Galaxy S20", "Samsung", DeviceType.MOBILE, Period.ZERO.toString(), 300000.0, "white", 128);
        Customer customer1 = new Customer("Alice", "Smith", "alice@example.com", "123 Main St", "Budapest", LocalDate.of(1990, 5, 15), PREMIUM);
        Device deviceMobile = new Device(deviceMobileTemplate, LocalDateTime.now().minus(Period.ofMonths(1)), customer1);
        deviceMobile.setId(1L);
        Mockito.when(deviceTemplateRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(deviceMobileTemplate));
        Mockito.when(deviceRepository.save(Mockito.any(Device.class))).thenReturn(deviceMobile);

        DeviceRequest deviceRequest = new DeviceRequest(deviceMobile.getId(), 1, LocalDateTime.now().minus(Period.ofMonths(1)));
        ResponseEntity<ApiResponse> response = deviceService.addDevices(deviceRequest);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
