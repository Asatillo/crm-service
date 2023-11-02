package com.example.CRM.repository;

import com.example.CRM.model.Customer;
import com.example.CRM.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s FROM Subscription s WHERE s.customer.id = ?1")
    List<Subscription> findByCustomerId(Long id);

    @Query("SELECT s.customer FROM Subscription s WHERE s.plan.id = ?1")
    List<Customer> getCustomersByPlanId(Long id);
}
