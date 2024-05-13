package com.example.CRM.service;

import com.example.CRM.model.Service;
import com.example.CRM.model.enums.DeviceType;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.repository.ServiceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.example.CRM.model.enums.ServiceTypes.*;
import static com.example.CRM.model.enums.ServiceTypes.DATA;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceTest {
    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceService serviceService;

    @Test
    public void ServiceService_CreateService_ReturnsService() {
        Service serviceMobile1 = new Service("1.5 GB Internet", DATA, 1.5F, DeviceType.MOBILE, 2000D);
        when(serviceRepository.save(Mockito.any(Service.class))).thenReturn(serviceMobile1);

        Service service = serviceService.addService(serviceMobile1).getBody();

        Assertions.assertNotNull(service);
    }

    @Test
    public void ServiceService_GetServiceById_ReturnsService() {
        Service serviceMobile1 = new Service("1.5 GB Internet", DATA, 1.5F, DeviceType.MOBILE, 2000D);
        when(serviceRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(serviceMobile1));

        Service service = serviceService.getById(1L).getBody();
        Assertions.assertNotNull(service);
        Assertions.assertEquals(service.getName(), serviceMobile1.getName());
    }

    @Test
    public void ServiceService_GetAllServices_ReturnsPagedResponse(){

        Service serviceMobile1 = new Service("1.5 GB Internet", DATA, 1.5F, DeviceType.MOBILE, 2000D);
        Service serviceMobile2 = new Service("1 hour of Voice", VOICE, 60F, DeviceType.MOBILE, 1000D);
        Service serviceMobile3 = new Service("100 SMS", SMS, 100F, DeviceType.MOBILE, 2500D);
        Page<Service> servicesPage = new PageImpl<>(List.of(serviceMobile1, serviceMobile2, serviceMobile3));
        PagedResponse<Service> servicePagedResponse = new PagedResponse<>(servicesPage);
        when(serviceRepository.findAllWithSearch(anyString(), Mockito.any(Pageable.class))).thenReturn(servicesPage);


        PagedResponse<Service> services = serviceService.getAll(0, 10, "id", "");
        Assertions.assertNotNull(services);
        Assertions.assertEquals(servicePagedResponse, services);
    }
}
