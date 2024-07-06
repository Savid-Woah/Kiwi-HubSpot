package instrumental.kiwi.customer.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.customer.dto.CustomerDTO;
import instrumental.kiwi.customer.service.CustomerService;
import instrumental.kiwi.exception.MessageTextResolver;
import instrumental.kiwi.security.auth.service.AuthService;
import instrumental.kiwi.security.config.filter.JwtAuthenticationFilter;
import instrumental.kiwi.security.config.service.JwtService;
import instrumental.kiwi.security.config.service.OAuth2AuthenticationSuccessHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;
    @MockBean
    private MessageTextResolver messageTextResolver;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private OAuth2AuthenticationSuccessHandler auth2AuthenticationSuccessHandler;
    @MockBean
    private CustomerService customerService;

    @Test
    @DisplayName("Should return customers page by store ID")
    void shouldReturnCustomersPageByStoreId() throws Exception {

        // Given
        Long storeId = 1L;
        Integer pageNumber = 0;
        Integer pageSize = 10;

        CustomerDTO customer1 = new CustomerDTO(0L);
        CustomerDTO customer2 = new CustomerDTO(1L);
        List<CustomerDTO> customers = List.of(customer1, customer2);
        Page<CustomerDTO> customersPage =new PageImpl<>(
                customers,
                PageRequest.of(pageNumber, pageSize),
                customers.size()
        );

        // When "mocked behaviour"
        Mockito.when(customerService.getAllCustomersByStore(storeId, pageNumber, pageSize)).thenReturn(customersPage);

        // and "endpoint hit"
        mockMvc.perform(get("/kiwi/api/v1/customers/get-by-store/{customer-id}/{page-number}/{page-size}",
                        storeId, pageNumber, pageSize))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        // Then
        verify(customerService, times(1)).getAllCustomersByStore(storeId, pageNumber, pageSize);
    }
}