package com.example.CRM.service;

import com.example.CRM.data.Customer;
import com.example.CRM.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer getOne(Long id) {
        return customerRepository.findById(id).orElse(null);
    }
}
