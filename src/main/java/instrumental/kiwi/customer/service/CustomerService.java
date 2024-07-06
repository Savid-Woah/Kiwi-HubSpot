package instrumental.kiwi.customer.service;

import instrumental.kiwi.customer.dto.CustomerDTO;
import instrumental.kiwi.customer.mapper.CustomerDTOMapper;
import instrumental.kiwi.customer.model.Customer;
import instrumental.kiwi.customer.repository.CustomerRepository;
import instrumental.kiwi.customer.request.CustomerRequest;
import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.security.user.model.User;
import instrumental.kiwi.store.model.Store;
import instrumental.kiwi.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static instrumental.kiwi.exception.MsgCode.OOPS_ERROR;
import static instrumental.kiwi.exception.MsgCode.STORE_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {

    private final StoreRepository storeRepository;
    private final CustomerDTOMapper customerDTOMapper;
    private final CustomerRepository customerRepository;

    public Page<CustomerDTO> getAllCustomersByStore(Long storeId, Integer pageNumber, Integer pageSize) {

        Store store = storeRepository.findById(storeId).orElseThrow(()
                -> new BackendException(OOPS_ERROR));

        return customerRepository.findAllByStore(store, PageRequest.of(pageNumber, pageSize))
                .map(customerDTOMapper);
    }

    public CustomerDTO createCustomer(CustomerRequest customerRequest, User user) {

        Customer customer = buildStudent(customerRequest, user);
        Customer savedCustomer = customerRepository.save(customer);
        return customerDTOMapper.apply(savedCustomer);
    }

    private Customer buildStudent(CustomerRequest customerRequest, User user) {

        Customer customer = new Customer(customerRequest, user);
        setCustomerRelations(customerRequest, customer);
        return customer;
    }

    private void setCustomerRelations(CustomerRequest customerRequest, Customer customer) {

        Long storeId = customerRequest.getStoreId();

        Store store = storeRepository.findById(storeId).orElseThrow(()
                -> new BackendException(STORE_NOT_FOUND));

        customer.setStore(store);
    }
}