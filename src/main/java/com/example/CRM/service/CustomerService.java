package com.example.CRM.service;

import com.example.CRM.exceptions.ExistingResourceException;
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
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);

        Page<Customer> customers = customerRepository.findAll(pageable);

        if(customers.getNumberOfElements() == 0){
            return new PagedResponse<>(List.of(), customers.getNumber(), customers.getSize(),
                    customers.getTotalElements(), customers.getTotalPages(), customers.isLast());
        }

        List<Customer> customerList = customers.getContent();

        return new PagedResponse<>(customerList, customers.getNumber(), customers.getSize(),
                customers.getTotalElements(), customers.getTotalPages(), customers.isLast());
    }

    public ResponseEntity<Customer> getById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    // not validating name, last name and address since the agent is the one who writes down the info from the customer's ID
    public ResponseEntity<Customer> addCustomer(Customer customer) {
        validateCustomer(customer);
        return new ResponseEntity<>(customerRepository.save(customer), HttpStatus.CREATED);
    }

    public ResponseEntity<Customer> updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        validateCustomer(customer);
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setCity(customer.getCity());
        existingCustomer.setDob(customer.getDob());
        existingCustomer.setSegment(customer.getSegment());
        return new ResponseEntity<>(customerRepository.save(existingCustomer), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deleteCustomer(Long id) {
        customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        List<Subscription> subscriptions = subscriptionRepository.findAllByCustomerId(id);
        for(Subscription subscription : subscriptions){
            subscriptionRepository.delete(subscription);
        }

        customerRepository.deleteById(id);
        return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Customer deleted successfully"), HttpStatus.OK);
    }

    // TODO: might need it in global
    private void validateCustomer(Customer customer) {
        if (customer.getPhoneNumber() != null && customer.getEmail() != null){
            if (customerRepository.existsByEmail(customer.getEmail())) {
                throw new ExistingResourceException("Customer", "email", customer.getEmail());
            }
            if (customerRepository.existsByPhoneNumber(customer.getPhoneNumber())) {
                throw new ExistingResourceException("Customer", "phone number", customer.getPhoneNumber());
            }

            if (!AppUtils.isValidEmail(customer.getEmail())) {
                throw new InvalidInputException("Customer", "email", customer.getEmail());
            }
            if (!AppUtils.isValidPhoneNumber(customer.getPhoneNumber())) {
                throw new InvalidInputException("Customer", "phone number", customer.getPhoneNumber());
            }


        }else {
            String field = customer.getPhoneNumber() == null ? "phoneNumber" : "email";
            throw new InvalidInputException("Customer", field, "null");
        }
    }
}
