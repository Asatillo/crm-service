package com.example.CRM.repository;

import com.example.CRM.model.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Page<Plan> findAllByDesignatedDeviceType(String deviceType, Pageable pageable);
}
