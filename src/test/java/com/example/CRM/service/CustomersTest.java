package com.example.CRM.service;

import com.example.CRM.exceptions.ApiException;
import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.Customer;
import com.example.CRM.model.enums.SegmentTypes;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.repository.CustomerRepository;
import com.example.CRM.repository.SubscriptionRepository;
import com.example.CRM.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static com.example.CRM.model.enums.SegmentTypes.EXPLORE;
import static com.example.CRM.model.enums.SegmentTypes.PREMIUM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomersTest {

    @Autowired
    private CustomerService customerService;

    @Test
    public void testGetAllCustomers_ValidPagination_ReturnsPagedResponse() {
        // Mock customer repository
        CustomerRepository mockCustomerRepository = Mockito.mock(CustomerRepository.class);
        SubscriptionRepository mockSubscriptionRepository = Mockito.mock(SubscriptionRepository.class);
        Page<Customer> mockPage = Mockito.mock(Page.class);
        when(mockCustomerRepository.searchCustomers(Mockito.any(Pageable.class), Mockito.anyString()))
                .thenReturn(mockPage);

        // Create customer service with mock
        CustomerService customerService = new CustomerService(mockCustomerRepository, mockSubscriptionRepository);

        // Set valid params
        int page = 1;
        int size = 10;
        String sort = "id";
        String search = "";
        String city = "";

        // Call the method
        PagedResponse<Customer> response = customerService.getAllCustomers(page, size, sort, search, city);

        // Assertions
        Mockito.verify(mockCustomerRepository).searchCustomers(Mockito.any(Pageable.class), Mockito.anyString());
        assertNotNull(response);
    }

    @Test
    public void testGetAllCustomers_InvalidPage_ThrowsException() {
        CustomerService customerService = new CustomerService(null, null);
        assertThrows(ApiException.class, () -> customerService.getAllCustomers(-1, 10, "id", "", ""));
    }

    @Test()
    public void testGetAllCustomers_InvalidSize_ThrowsException() {
        CustomerService customerService = new CustomerService(null, null);
        assertThrows(IllegalArgumentException.class, () -> customerService.getAllCustomers(1, 0, "id", "", ""));
    }

    @Test
    public void testGetById_ExistingId_ReturnsCustomer() {
        // Mock customer repository
        CustomerRepository mockCustomerRepository = Mockito.mock(CustomerRepository.class);
        Customer mockCustomer = Mockito.mock(Customer.class);
        when(mockCustomerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));

        // Create customer service with mock
        CustomerService customerService = new CustomerService(mockCustomerRepository, null);

        // Call the method
        ResponseEntity<Customer> response = customerService.getById(1L);

        // Assertions
        Mockito.verify(mockCustomerRepository).findById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetById_NonExistingId_ThrowsException() {
        // Mock customer repository
        CustomerRepository mockCustomerRepository = Mockito.mock(CustomerRepository.class);
        when(mockCustomerRepository.findById(1L)).thenReturn(Optional.empty());

        // Create customer service with mock
        CustomerService customerService = new CustomerService(mockCustomerRepository, null);

        // Call the method
        assertThrows(ResourceNotFoundException.class, () -> customerService.getById(1L));
    }

    @Test
    public void testAddCustomer_ValidCustomer_ReturnsCreated() {
        // Mock customer repository
        CustomerRepository mockCustomerRepository = Mockito.mock(CustomerRepository.class);
        Customer customer = new Customer();
        when(mockCustomerRepository.save(customer)).thenReturn(customer);

        // Create customer service with mock
        CustomerService customerService = new CustomerService(mockCustomerRepository, null);

        // Call the method
        ResponseEntity<Customer> response = customerService.addCustomer(customer);

        // Assertions
        Mockito.verify(mockCustomerRepository).save(customer);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testUpdateCustomer_ExistingId_ReturnsUpdatedCustomer() {
        // Mock customer repository
        CustomerRepository mockCustomerRepository = Mockito.mock(CustomerRepository.class);
        Customer existingCustomer = new Customer("Alice", "Smith", "alice@example.com",
                "123 Main St", "Budapest", LocalDate.of(1990, 5, 15), PREMIUM);
        existingCustomer.setId(1L);
        when(mockCustomerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(mockCustomerRepository.save(existingCustomer)).thenReturn(existingCustomer);

        // Create customer service with mock
        CustomerService customerService = new CustomerService(mockCustomerRepository, null);
        Customer updateCustomer = new Customer("newFirstName", "newLastName", "new@email.com",
                "newAddress", "newCity", LocalDate.of(1990, 2, 1), EXPLORE);

        // Call the method
        ResponseEntity<Customer> response = customerService.updateCustomer(1L, updateCustomer);

        // Assertions
        Mockito.verify(mockCustomerRepository).findById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Assert updated fields on returned customer
        assertEquals("new@email.com", response.getBody().getEmail());
        assertEquals(SegmentTypes.EXPLORE, response.getBody().getSegment());
         assertEquals(updateCustomer.getDob(), response.getBody().getDob());  // Uncomment if using LocalDate comparison library
        assertEquals("newCity", response.getBody().getCity());
        assertEquals("newAddress", response.getBody().getAddress());
        assertEquals("newFirstName", response.getBody().getFirstName());
        assertEquals("newLastName", response.getBody().getLastName());
    }

    @Test
    public void testUpdateCustomer_NonExistingId_ThrowsException() {
        // Mock customer repository
        CustomerRepository mockCustomerRepository = Mockito.mock(CustomerRepository.class);
        when(mockCustomerRepository.findById(1L)).thenReturn(Optional.empty());

        // Create customer service with mock
        CustomerService customerService = new CustomerService(mockCustomerRepository, null);
        Customer updateCustomer = new Customer();

        // Call the method
        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomer(1L, updateCustomer));
    }
}
