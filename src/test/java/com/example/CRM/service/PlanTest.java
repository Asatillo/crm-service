package com.example.CRM.service;

import com.example.CRM.model.Plan;
import com.example.CRM.model.Service;
import com.example.CRM.model.enums.DeviceType;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.payload.request.PlanRequest;
import com.example.CRM.repository.PlanRepository;
import com.example.CRM.repository.ServiceRepository;
import com.example.CRM.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.CRM.model.enums.ServiceTypes.*;
import static com.example.CRM.model.enums.ServiceTypes.SMS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanTest {
    @Mock
    private PlanRepository planRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private ServiceRepository serviceRepository;
    @InjectMocks
    private PlanService planService;

    @Test
    public void PlanService_GetAllPlans_ReturnsAllPlans() {
        Service serviceMobile1 = new Service("1.5 GB Internet", DATA, 1.5F, DeviceType.MOBILE, 2000D);
        Service serviceMobile2 = new Service("1 hour of Voice", VOICE, 60F, DeviceType.MOBILE, 1000D);
        Service serviceMobile3 = new Service("100 SMS", SMS, 100F, DeviceType.MOBILE, 2500D);
        Service serviceMobile4 = new Service("3 GB Internet", DATA, 3F, DeviceType.MOBILE, 2500D);
        Service serviceMobile5 = new Service("2 hours of Voice", VOICE, 120F, DeviceType.MOBILE, 1800D);
        Service serviceMobile6 = new Service("200 SMS", SMS, 200F, DeviceType.MOBILE, 3000D);

        Plan planMobile1 = new Plan("Basic Plan", Period.ofYears(1).toString(), "Basic subscription", 1999., List.of(serviceMobile1, serviceMobile2, serviceMobile3), DeviceType.MOBILE);
        Plan planMobile2 = new Plan("Premium Plan", Period.ofMonths(3).toString(), "Premium subscription", 3999., List.of(serviceMobile4, serviceMobile5, serviceMobile6), DeviceType.MOBILE);

        Page<Plan> plansPage = new PageImpl<>(List.of(planMobile1, planMobile2));
        when(planRepository.findAllWithSearch(anyString(), any(Pageable.class))).thenReturn(plansPage);

        PagedResponse<Plan> pagedResponse = planService.getAll(0, 10, "id", "");
        assertNotNull(pagedResponse);
        assertEquals(pagedResponse.getContent().size(), 2);
        assertEquals(pagedResponse.getContent().get(0).getName(), "Basic Plan");
    }

    @Test
    public void PlanService_GetPlanById_ReturnsDevice(){
        Service serviceMobile1 = new Service("1.5 GB Internet", DATA, 1.5F, DeviceType.MOBILE, 2000D);
        Service serviceMobile2 = new Service("1 hour of Voice", VOICE, 60F, DeviceType.MOBILE, 1000D);
        Service serviceMobile3 = new Service("100 SMS", SMS, 100F, DeviceType.MOBILE, 2500D);

        Plan planMobile1 = new Plan("Basic Plan", Period.ofYears(1).toString(), "Basic subscription", 1999., List.of(serviceMobile1, serviceMobile2, serviceMobile3), DeviceType.MOBILE);
        when(planRepository.findById(any(Long.class))).thenReturn(Optional.of(planMobile1));

        ResponseEntity<Plan> response = planService.getById(1L);
        assertNotNull(response);
        assertEquals(Objects.requireNonNull(response.getBody()).getName(), "Basic Plan");
    }

    @Test
    public void PlanService_CreatePlan_ReturnsPlan(){
        Service serviceMobile1 = new Service("1.5 GB Internet", DATA, 1.5F, DeviceType.MOBILE, 2000D);
        serviceMobile1.setId(1L);
        Plan planMobile1 = new Plan("Basic Plan", Period.ofYears(1).toString(), "Basic subscription", 1999., List.of(serviceMobile1), DeviceType.MOBILE);
        when(planRepository.save(any(Plan.class))).thenReturn(planMobile1);
        when(serviceRepository.findById(any(Long.class))).thenReturn(Optional.of(serviceMobile1));

        PlanRequest planRequest = new PlanRequest("Basic Plan", Period.ofYears(1).toString(), "Basic subscription", 1999., DeviceType.MOBILE, List.of(serviceMobile1.getId()));
        ResponseEntity<Plan> response = planService.addPlan(planRequest);
        assertNotNull(response);
        assertEquals(Objects.requireNonNull(response.getBody()).getName(), "Basic Plan");
    }
}
