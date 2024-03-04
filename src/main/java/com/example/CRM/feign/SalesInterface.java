package com.example.CRM.feign;

import com.example.CRM.payload.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;

@FeignClient(name = "SALES-SERVICE", url = "http://localhost:8765", path = "/sales/orders")
public interface SalesInterface {
    @PostMapping
    ResponseEntity<HashMap> add(@RequestHeader (value = "Authorization") String authHeader, @RequestBody HashMap Sale);
}
