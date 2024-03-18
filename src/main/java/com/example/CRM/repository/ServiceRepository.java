package com.example.CRM.repository;

import com.example.CRM.model.Service;
import com.example.CRM.model.enums.DeviceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    Page<Service> findByDesignatedDeviceType(DeviceType deviceType, Pageable pageable);

    @Query("SELECT s FROM Service s WHERE CONCAT(s.name, ' ', s.type, ' ', s.designatedDeviceType) LIKE %?1%")
    Page<Service> findAllWithSearch(String search, Pageable pageable);
}
