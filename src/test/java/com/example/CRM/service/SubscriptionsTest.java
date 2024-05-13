package com.example.CRM.service;

import com.example.CRM.feign.SalesInterface;
import com.example.CRM.model.*;
import com.example.CRM.model.enums.DeviceType;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.request.SubscriptionRequest;
import com.example.CRM.repository.DeviceRepository;
import com.example.CRM.repository.NetworkEntityRepository;
import com.example.CRM.repository.PlanRepository;
import com.example.CRM.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.example.CRM.model.enums.SegmentTypes.PREMIUM;
import static com.example.CRM.model.enums.ServiceTypes.DATA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionsTest {
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private NetworkEntityRepository networkEntityRepository;
    @Mock
    private PlanRepository planRepository;
    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    SalesInterface salesInterface;
    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    public void SubscriptionService_GetAllSubscriptions_ReturnsAllSubscriptions() {
        Customer customer1 = new Customer("Alice", "Smith", "alice@example.com", "123 Main St", "Budapest", LocalDate.of(1990, 5, 15), PREMIUM);
        Service serviceMobile1 = new Service("1.5 GB Internet", DATA, 1.5F, DeviceType.MOBILE, 2000D);
        Plan planMobile1 = new Plan("Basic Plan", Period.ofYears(1).toString(), "Basic subscription", 1999., List.of(serviceMobile1),DeviceType.MOBILE);
        NetworkEntity networkMobile1 = new NetworkEntity("+36201234567", DeviceType.MOBILE, customer1, "Personal Mobile");
        Subscription subscription1 = new Subscription(networkMobile1, planMobile1, LocalDate.now());
    }

    @Test
    public void SubscriptionService_GetById_ReturnsSubscription() {
        Customer customer1 = new Customer("Alice", "Smith", "alice@example.com", "123 Main St", "Budapest", LocalDate.of(1990, 5, 15), PREMIUM);
        Service serviceMobile1 = new Service("1.5 GB Internet", DATA, 1.5F, DeviceType.MOBILE, 2000D);
        Plan planMobile1 = new Plan("Basic Plan", Period.ofYears(1).toString(), "Basic subscription", 1999., List.of(serviceMobile1),DeviceType.MOBILE);
        NetworkEntity networkMobile1 = new NetworkEntity("+36201234567", DeviceType.MOBILE, customer1, "Personal Mobile");
        Subscription subscription1 = new Subscription(networkMobile1, planMobile1, LocalDate.now());
        when(subscriptionRepository.findById(any(Long.class))).thenReturn(Optional.of(subscription1));

        ResponseEntity<Subscription> response = subscriptionService.getById(1L);
        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), 200);
        assertEquals(response.getBody(), subscription1);
    }

    @Test
    public void SubscriptionService_AddSubscription_ReturnsSubscription() {
        Customer customer1 = new Customer("Alice", "Smith", "alice@example.com", "123 Main St", "Budapest", LocalDate.of(1990, 5, 15), PREMIUM);
        Service serviceMobile1 = new Service("1.5 GB Internet", DATA, 1.5F, DeviceType.MOBILE, 2000D);
        Plan planMobile1 = new Plan("Basic Plan", Period.ofYears(1).toString(), "Basic subscription", 1999., List.of(serviceMobile1),DeviceType.MOBILE);
        NetworkEntity networkMobile1 = new NetworkEntity("+36201234567", DeviceType.MOBILE, customer1, "Personal Mobile");
        Subscription subscription1 = new Subscription(networkMobile1, planMobile1, LocalDate.now());
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription1);
        when(planRepository.findById(any(Long.class))).thenReturn(Optional.of(planMobile1));
        when(networkEntityRepository.findById(any(Long.class))).thenReturn(Optional.of(networkMobile1));
        ResponseEntity<HashMap> salesResponse = new ResponseEntity<>(new HashMap<>(), HttpStatus.CREATED);
        when(salesInterface.add(any(String.class), any(HashMap.class))).thenReturn(salesResponse);

        SubscriptionRequest subscriptionRequest = new SubscriptionRequest(1L, 1L, null, LocalDate.now());
        ResponseEntity<Subscription> response = subscriptionService.addSubscription(subscriptionRequest, "Bearer token");

        assertNotNull(response);
        assertEquals(response.getStatusCode().value(), 201);
    }
}
