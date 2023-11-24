package com.example.CRM.repository;

import com.example.CRM.model.Device;
import com.example.CRM.model.template.DeviceTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByDeviceTemplate(DeviceTemplate deviceTemplate);

    Page<Device> findByDeviceTemplate_Id(Long id, Pageable pageable);

    Page<Device> findByDeviceTemplate_DeviceType(String deviceType, Pageable pageable);
}
