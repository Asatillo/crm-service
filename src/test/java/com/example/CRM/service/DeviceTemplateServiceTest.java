package com.example.CRM.service;

import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.enums.DeviceType;
import com.example.CRM.model.template.DeviceTemplate;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.repository.DeviceRepository;
import com.example.CRM.repository.DeviceTemplateRepository;
import com.example.CRM.service.DeviceTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceTemplateServiceTest {

    @Mock private DeviceTemplateRepository deviceTemplateRepository;
    @Mock private DeviceRepository deviceRepository;

    @InjectMocks private DeviceTemplateService deviceTemplateService;

    private DeviceTemplate deviceTemplate;

    @BeforeEach public void setUp() {
        deviceTemplate = new DeviceTemplate();
        deviceTemplate.setId(1L);
        deviceTemplate.setBrand("BrandA");
        deviceTemplate.setModel("ModelX");
        deviceTemplate.setDeviceType(DeviceType.MOBILE);
        deviceTemplate.setStorage(256);
        deviceTemplate.setWarrantyDuration("1Y");
    }

    @Test
    public void testGetAllDeviceTemplates_whenPaginateTrue_expectList() {
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.ASC, "brand");
        List<DeviceTemplate> templates = Arrays.asList(deviceTemplate);
        Page<DeviceTemplate> page = new PageImpl<>(templates, pageable, templates.size());

        when(deviceTemplateRepository.findAll(pageable)).thenReturn(page);
        PagedResponse<DeviceTemplate> result = deviceTemplateService.getAllDeviceTemplates(0, 5, "brand", true);

        assertEquals(1, result.getTotalElements());
        assertEquals(deviceTemplate, result.getContent().get(0));
    }

    @Test public void testGetAllDeviceTemplates_whenPaginateFalse_expectList() {
        List<DeviceTemplate> templates = Arrays.asList(deviceTemplate);

        when(deviceTemplateRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(templates));
        PagedResponse<DeviceTemplate> result = deviceTemplateService.getAllDeviceTemplates(0, 5, "brand", false);

        assertEquals(1L, result.getTotalElements());
        assertEquals(deviceTemplate, result.getContent().get(0));
    }

    @Test
    public void testGetById_found_expectDeviceTemplate() {
        when(deviceTemplateRepository.findById(1L)).thenReturn(Optional.of(deviceTemplate));
        ResponseEntity<DeviceTemplate> result = deviceTemplateService.getById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(deviceTemplate, result.getBody());
    }

    @Test public void testGetById_notFound_expectResourceNotFoundException() {
        when(deviceTemplateRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            deviceTemplateService.getById(1L);
        });
    }

    @Test public void testCreateDeviceTemplate_expectCreated() {
        when(deviceTemplateRepository.save(deviceTemplate)).thenReturn(deviceTemplate);

        ResponseEntity<DeviceTemplate> result = deviceTemplateService.createDeviceTemplate(deviceTemplate);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(deviceTemplate, result.getBody());
    }

    @Test
    public void testDeleteDeviceTemplate_existAndNoReference_expectSuccess() {
        when(deviceTemplateRepository.findById(1L)).thenReturn(Optional.of(deviceTemplate));
        when(deviceRepository.findByDeviceTemplate(deviceTemplate)).thenReturn(new ArrayList<>());

        ResponseEntity<ApiResponse> result = deviceTemplateService.deleteDeviceTemplate(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().getSuccess());
        assertEquals("Device Template deleted successfully", result.getBody().getMessage());
    }
}
