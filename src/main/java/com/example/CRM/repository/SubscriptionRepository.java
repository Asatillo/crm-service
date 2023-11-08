package com.example.CRM.repository;

import com.example.CRM.model.Customer;
import com.example.CRM.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllByCustomerId(Long id);

    List<Customer> getCustomersByPlanId(Long id);
}
