package com.example.CRM.repository;

import com.example.CRM.model.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Page<Plan> findAllByDesignatedDeviceType(String deviceType, Pageable pageable);

    @Query("SELECT p FROM Plan p WHERE p.isActive AND p.designatedDeviceType = :deviceType AND (p.name LIKE %:search% OR p.description LIKE %:search%)")
    List<Plan> findAllByActiveTrue(String deviceType, String search);

    @Query("SELECT p FROM Plan p WHERE p.isActive AND p.designatedDeviceType = :deviceType AND (p.name LIKE %:search% OR p.description LIKE %:search%)")
    Page<Plan> findAllByActiveTrue(Pageable pageable, String deviceType, String search);

    @Query("SELECT p FROM Plan p WHERE CONCAT(p.name, '', p.description) LIKE %:search%")
    Page<Plan> findAllWithSearch(String search, Pageable pageable);
}
