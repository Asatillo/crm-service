package com.example.CRM.repository;

import com.example.CRM.model.Device;
import com.example.CRM.model.template.DeviceTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByDeviceTemplate(DeviceTemplate deviceTemplate);

    Page<Device> findByDeviceTemplate_Id(Long id, Pageable pageable);

    Page<Device> findByDeviceTemplate_DeviceType(String deviceType, Pageable pageable);

    @Query("SELECT d FROM Device d WHERE d.deviceTemplate.deviceType = ?1  and CONCAT(d.deviceTemplate.brand, ' ', d.deviceTemplate.model, ' ', d.deviceTemplate.storage) LIKE %?2%")
    Page<Device> searchDevicesByDeviceType(Pageable pageable, String deviceType, String search);
}
