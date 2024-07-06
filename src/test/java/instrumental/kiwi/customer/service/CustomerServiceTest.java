package instrumental.kiwi.customer.service;

import instrumental.kiwi.customer.dto.CustomerDTO;
import instrumental.kiwi.customer.mapper.CustomerDTOMapper;
import instrumental.kiwi.customer.model.Customer;
import instrumental.kiwi.customer.repository.CustomerRepository;
import instrumental.kiwi.customer.request.CustomerRequest;
import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.exception.MsgCode;
import instrumental.kiwi.security.user.model.User;
import instrumental.kiwi.store.model.Store;
import instrumental.kiwi.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static instrumental.kiwi.security.user.enums.Role.CUSTOMER;
import static instrumental.kiwi.security.user.enums.Source.SYSTEM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Spy
    private CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();
    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName(value = "Should return a page of customer dto")
    void shouldReturnCustomerDTOPage() {

        // Given "an existing store"
        Long storeId = 0L;
        Store store = new Store();
        store.setStoreId(storeId);

        // and "a page number and page size"
        int pageNumber = 0;
        int pageSize = 1;

        // Create mock User instances
        User mockUser1 = mock(User.class);
        User mockUser2 = mock(User.class);

        // and "some customers"
        Customer samuel = Customer
                .builder()
                .customerId(0L)
                .user(mockUser1)
                .store(store)
                .build();

        Customer david = Customer
                .builder()
                .customerId(0L)
                .user(mockUser2)
                .store(store)
                .build();

        // and "those customers put in a list"
        List<Customer> customers = new ArrayList<>(List.of(samuel, david));

        // and "a page made out of the list with the parameters given"
        Page<Customer> customersPage = new PageImpl<>(
                customers,
                PageRequest.of(pageNumber, pageSize),
                customers.size()
        );

        // and "the page mapped from Customer to CustomerDTO"
        Page<CustomerDTO> customerDTOPage = customersPage
                .map(customerDTOMapper);

        // when "mocked behaviour"
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(customerRepository.findAllByStore(store, PageRequest.of(pageNumber, pageSize))).thenReturn(customersPage);

        // and "service called"
        var result = customerService.getAllCustomersByStore(storeId, pageNumber, pageSize);

        // then
        assertEquals(result, customerDTOPage);
    }

    @Test
    @DisplayName(value = "Should create customer")
    void shouldCreateCustomer() {

        // Given "an existing store"
        Long storeId = 0L;
        Store store = new Store();
        store.setStoreId(storeId);

        // and "an existing user"
        User user = new User();
        user.setEmail("store@gmail.com");
        user.setPassword("0000");
        user.setSource(SYSTEM);
        user.setRole(CUSTOMER);

        // and "a customer request"
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("Samuel");
        customerRequest.setLastName("Ballesteros");
        customerRequest.setPhoneNumber("3225447725");
        customerRequest.setStoreId(storeId);

        // and "a customer built from the request"
        Customer customer = new Customer(customerRequest, user);
        customer.setStore(store);

        // and "a saved customer to be returned by the repository"
        Customer savedCustomer = new Customer(customerRequest, user);
        savedCustomer.setCustomerId(0L);
        savedCustomer.setStore(store);

        // When "mocked behaviour"
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(customerRepository.save(customer)).thenReturn(savedCustomer);

        // and "service called"
        customerService.createCustomer(customerRequest, user);

        // Then
        verify(storeRepository, times(1)).findById(storeId);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    @DisplayName("Should throw exception on store not found")
    void shouldThrowExceptionOnStoreNotFound() {

        // Given "an existing store id"
        Long storeId = 0L;

        // and "an existing user"
        User user = new User();
        user.setEmail("customer@gmail.com");
        user.setPassword("0000");
        user.setSource(SYSTEM);
        user.setRole(CUSTOMER);

        // and "a customer request"
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("Samuel");
        customerRequest.setLastName("Ballesteros");
        customerRequest.setPhoneNumber("3225447725");
        customerRequest.setStoreId(storeId);

        // Mocked Behaviour
        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // When & Then
        BackendException exception = assertThrows(BackendException.class, () ->
                customerService.createCustomer(customerRequest, user));

        assertEquals(MsgCode.STORE_NOT_FOUND, exception.getMsgCode());

        // Verify
        verify(storeRepository, times(1)).findById(storeId);
        verifyNoMoreInteractions(storeRepository, customerRepository);
    }
}