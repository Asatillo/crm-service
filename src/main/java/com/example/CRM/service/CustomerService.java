package com.example.CRM.service;

import com.example.CRM.model.Customer;
import com.example.CRM.model.Segment;
import com.example.CRM.repository.CustomerRepository;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public ResponseEntity<List<Customer>> getAll() {
        return new ResponseEntity<>(customerRepository.findAll(), HttpStatus.OK);
    }

    // TODO: data validation
    public ResponseEntity<Customer> getById(Long id) {
        return new ResponseEntity<>(customerRepository.findById(id).orElse(null), HttpStatus.OK);
    }

    // TODO: data validation
    public ResponseEntity<Customer> addCustomer(Customer customer) {
        return new ResponseEntity<>(customerRepository.save(customer), HttpStatus.CREATED);
    }

    //  TODO: data validation
    public ResponseEntity<Customer> updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setCity(customer.getCity());
        existingCustomer.setDob(customer.getDob());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        existingCustomer.setSegment(customer.getSegment());
        existingCustomer.setEmail(customer.getEmail());
        return new ResponseEntity<>(customerRepository.save(existingCustomer), HttpStatus.OK);
    }

    // TODO: validate
    public ResponseEntity<Long> deleteCustomer(Long id) {
        customerRepository.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
