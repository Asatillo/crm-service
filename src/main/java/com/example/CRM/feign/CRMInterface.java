package com.example.CRM.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "SALES-SERVICE", url = "http://localhost:8765")
public interface CRMInterface {
    // the methods to be used in the SALE-SERVICE microservice
}
