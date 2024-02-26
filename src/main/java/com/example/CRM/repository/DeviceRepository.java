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

    @Query("SELECT d FROM Device d WHERE d.deviceTemplate.deviceType = ?1  and CONCAT(d.deviceTemplate.brand, ' ', d.deviceTemplate.model, ' ', d.deviceTemplate.storage) LIKE %?2%")
    Page<Device> searchDevicesByDeviceType(Pageable pageable, String deviceType, String search);

    @Query("SELECT d FROM Device d WHERE d.owner.id = ?1 and CONCAT(d.deviceTemplate.brand, ' ', d.deviceTemplate.model, ' ', d.deviceTemplate.storage) LIKE %?2%")
    Page<Device> findAllDevicesByOwner_Id(Long id, String search, Pageable pageable);

    @Query("SELECT d FROM Device d WHERE d.owner=null and d.deviceTemplate.deviceType = ?2 and CONCAT(d.deviceTemplate.brand, ' ', d.deviceTemplate.model, ' ', d.deviceTemplate.storage) LIKE %?1%")
    List<Device> findAllAvailableDevices(String search, String type);

    @Query("SELECT d FROM Device d WHERE d.owner=null and d.deviceTemplate.deviceType = ?2 and CONCAT(d.deviceTemplate.brand, ' ', d.deviceTemplate.model, ' ', d.deviceTemplate.storage) LIKE %?1%")
    Page<Device> findAllAvailableDevices(String search, String type, Pageable pageable);
}
