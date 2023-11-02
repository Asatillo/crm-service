package com.example.CRM.service;

import com.example.CRM.model.Customer;
import com.example.CRM.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public ResponseEntity<List<Customer>> getAll() {
        return new ResponseEntity<>(customerRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Customer> getById(Long id) {
        return new ResponseEntity<>(customerRepository.findById(id).orElse(null), HttpStatus.OK);
    }
}
