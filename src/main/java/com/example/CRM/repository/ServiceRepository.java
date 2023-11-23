package com.example.CRM.repository;

import com.example.CRM.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    Page<Service> findByDesignatedDeviceType(String deviceType, Pageable pageable);
}
