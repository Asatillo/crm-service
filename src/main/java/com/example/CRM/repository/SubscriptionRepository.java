package com.example.CRM.repository;

import com.example.CRM.model.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Page<Subscription> findAllByNetworkEntity_Owner_Id(Long id, Pageable pageable);

    List<Subscription> findAllByNetworkEntity_Owner_Id(Long id);

    List<Subscription> findAllByPlanId(Long id);

    Page<Subscription> findAllByPlanId(Long id, Pageable pageable);

    boolean existsByPlan_Id(Long id);
}
