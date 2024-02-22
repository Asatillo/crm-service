package com.example.CRM.repository;

import com.example.CRM.model.template.DeviceTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceTemplateRepository extends JpaRepository<DeviceTemplate, Long> {

    Page<DeviceTemplate> findByBrand(String brand, Pageable pageable);

    @Query("SELECT dt FROM DeviceTemplate dt WHERE dt.deviceType = ?1 AND CONCAT(dt.brand, ' ', dt.model, ' ', dt.color) LIKE %?2%")
    Page<DeviceTemplate> searchByDeviceType(Pageable pageable, String deviceType, String search);
}
