package com.example.CRM.repository;

import com.example.CRM.model.template.DeviceTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceTemplateRepository extends JpaRepository<DeviceTemplate, Long> {

    Page<DeviceTemplate> findByBrand(String brand, Pageable pageable);

    Page<DeviceTemplate> findByIsMobileTrue(Pageable pageable);

    Page<DeviceTemplate> findByIsMobileFalse(Pageable pageable);
}
