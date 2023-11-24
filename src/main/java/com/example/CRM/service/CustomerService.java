package com.example.CRM.service;

import com.example.CRM.exceptions.InvalidInputException;
import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.Customer;
import com.example.CRM.model.Subscription;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.repository.CustomerRepository;
import com.example.CRM.repository.SubscriptionRepository;
import com.example.CRM.utils.AppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    final
    CustomerRepository customerRepository;

    final
    SubscriptionRepository subscriptionRepository;

    public CustomerService(CustomerRepository customerRepository, SubscriptionRepository subscriptionRepository) {
        this.customerRepository = customerRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public PagedResponse<Customer> getAllCustomers(int page, int size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, Customer.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<Customer> customers = customerRepository.findAll(pageable);
        PagedResponse<Customer> pagedResponse = new PagedResponse<>();

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());

        return pagedResponse.returnPagedResponse(customers);
    }

    public ResponseEntity<Customer> getById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    public ResponseEntity<Customer> addCustomer(Customer customer) {
        return new ResponseEntity<>(customerRepository.save(customer), HttpStatus.CREATED);
    }

    public ResponseEntity<Customer> updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        if(!customer.getEmail().equals(existingCustomer.getEmail())){
            existingCustomer.setEmail(customer.getEmail());
        }
        if(!customer.getSegment().equals(existingCustomer.getSegment())){
            existingCustomer.setSegment(customer.getSegment());
        }
        if(!customer.getDob().equals(existingCustomer.getDob())){
            AppUtils.validateDOB(customer.getDob());
            existingCustomer.setDob(customer.getDob());
        }
        if(!customer.getCity().equals(existingCustomer.getCity())){
            existingCustomer.setCity(customer.getCity());
        }
        if(!customer.getAddress().equals(existingCustomer.getAddress())){
            existingCustomer.setAddress(customer.getAddress());
        }
        if(!customer.getFirstName().equals(existingCustomer.getFirstName())){
            existingCustomer.setFirstName(customer.getFirstName());
        }
        if(!customer.getLastName().equals(existingCustomer.getLastName())){
            existingCustomer.setLastName(customer.getLastName());
        }
        return new ResponseEntity<>(customerRepository.save(existingCustomer), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deleteCustomer(Long id) {
        customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        List<Subscription> subscriptions = subscriptionRepository.findAllByNetworkEntity_Owner_Id(id);
        subscriptionRepository.deleteAll(subscriptions);

        customerRepository.deleteById(id);
        return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Customer deleted successfully"), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deactivateCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        if(!customer.isActive()){
            return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Customer is already deactivated"), HttpStatus.OK);
        }
        customer.setActive(false);
        customerRepository.save(customer);
        return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Customer deactivated successfully"), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> activateCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        if(customer.isActive()){
            return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Customer is already active"), HttpStatus.OK);
        }
        customer.setActive(true);
        customerRepository.save(customer);
        return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Customer activated successfully"), HttpStatus.OK);
    }
}
