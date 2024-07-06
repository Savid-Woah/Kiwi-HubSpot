package instrumental.kiwi.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.customer.request.CustomerRequest;
import instrumental.kiwi.exception.MessageTextResolver;
import instrumental.kiwi.security.auth.api.AuthController;
import instrumental.kiwi.security.auth.request.LoginRequest;
import instrumental.kiwi.security.auth.request.RegisterCustomerRequest;
import instrumental.kiwi.security.auth.request.RegisterStoreRequest;
import instrumental.kiwi.security.auth.service.AuthService;
import instrumental.kiwi.security.config.filter.JwtAuthenticationFilter;
import instrumental.kiwi.security.config.service.JwtService;
import instrumental.kiwi.security.config.service.OAuth2AuthenticationSuccessHandler;
import instrumental.kiwi.security.user.request.UserRequest;
import instrumental.kiwi.store.request.StoreRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static instrumental.kiwi.response.handler.ResponseHandler.generateResponse;
import static instrumental.kiwi.response.message.ResponseMessage.CUSTOMER_REGISTERED;
import static instrumental.kiwi.response.message.ResponseMessage.STORE_REGISTERED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

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
    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName(value = "Should login user")
    void shouldLoginUser() throws Exception {

        // given "a login request"
        LoginRequest loginRequest = LoginRequest
                .builder()
                .email("test@example.com")
                .password("password")
                .build();

        // and "a mocked token"
        String token = anyString();

        // when "mocked behaviour"
        when(authService.login(loginRequest, any(HttpServletResponse.class))).thenReturn(token);

        // and "endpoint hit" then "expect the following"
        mockMvc.perform(post("/kiwi/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }

    @Test
    @DisplayName(value = "Should register store")
    void shouldRegisterStore() throws Exception {

        // given "a user request"
        UserRequest userRequest = UserRequest
                .builder()
                .email("user@email.com")
                .password("user444")
                .build();

        // and "a store request"
        StoreRequest storeRequest = StoreRequest
                .builder()
                .name("Americana")
                .build();

        // and "a register store request"
        RegisterStoreRequest registerStoreRequest = RegisterStoreRequest
                .builder()
                .userRequest(userRequest)
                .storeRequest(storeRequest)
                .build();

        // and "an expected result"
        Map<String, Object> response = generateResponse(CREATED, STORE_REGISTERED);

        // when "mocked behaviour"
        when(authService.registerStore(registerStoreRequest)).thenReturn(response);

        // and "endpoint hit" then "expect the following"
        mockMvc.perform(post("/kiwi/api/v1/auth/register-store")
                        .content(objectMapper.writeValueAsString(registerStoreRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName(value = "Should register customer")
    void shouldRegisterCustomer() throws Exception {

        // given "an existing store id"
        Long storeId = 0L;

        // and "a user request"
        UserRequest userRequest = UserRequest
                .builder()
                .email("user@email.com")
                .password("user444")
                .build();

        // and "a customer request"
        CustomerRequest customerRequest = CustomerRequest
                .builder()
                .name("Samuel")
                .storeId(storeId)
                .build();

        // and "a register customer request"
        RegisterCustomerRequest registerCustomerRequest = RegisterCustomerRequest
                .builder()
                .userRequest(userRequest)
                .customerRequest(customerRequest)
                .build();

        // and "an expected result"
        Map<String, Object> response = generateResponse(CREATED, CUSTOMER_REGISTERED);

        // when "mocked behaviour"
        when(authService.registerCustomer(registerCustomerRequest)).thenReturn(response);

        // and "endpoint hit" then "expect the following"
        mockMvc.perform(post("/kiwi/api/v1/auth/register-customer")
                .content(objectMapper.writeValueAsString(registerCustomerRequest))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }
}