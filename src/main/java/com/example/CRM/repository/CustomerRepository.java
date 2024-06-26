package com.example.CRM.repository;

import com.example.CRM.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE CONCAT(c.firstName, ' ', c.lastName, ' ', c.segment) LIKE %?1%")
    Page<Customer> searchCustomers(Pageable pageable, String search);

    @Query("SELECT c FROM Customer c " +
            "WHERE CONCAT(c.firstName, ' ', c.lastName, ' ', c.segment) LIKE %?1%" +
            "AND c.city = ?2")
    Page<Customer> searchCustomersByCity(Pageable pageable, String search, String city);
}
