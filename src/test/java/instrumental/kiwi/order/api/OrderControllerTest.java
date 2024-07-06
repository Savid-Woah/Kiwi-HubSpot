package instrumental.kiwi.order.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.exception.MessageTextResolver;
import instrumental.kiwi.line_item.request.ItemPurchased;
import instrumental.kiwi.order.request.OrderRequest;
import instrumental.kiwi.order.service.OrderService;
import instrumental.kiwi.response.Response;
import instrumental.kiwi.security.auth.service.AuthService;
import instrumental.kiwi.security.config.filter.JwtAuthenticationFilter;
import instrumental.kiwi.security.config.service.JwtService;
import instrumental.kiwi.security.config.service.OAuth2AuthenticationSuccessHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

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
    private OrderService orderService;

    @Test
    @DisplayName("Should add an order successfully")
    void shouldAddOrderSuccessfully() throws Exception {

        // Given
        OrderRequest orderRequest = OrderRequest.builder()
                .hsOrderName("Order123")
                .hsCurrencyCode("USD")
                .hsSourceStore("Store1")
                .hsFulfillmentStatus("Pending")
                .hsShippingAddressCity("New York")
                .hsShippingAddressState("NY")
                .hsShippingAddressStreet("123 Main St")
                .itemsPurchased(List.of(
                        new ItemPurchased(1L, 2),
                        new ItemPurchased(2L, 1)
                ))
                .customerId(1L)
                .build();

        Response expectedResponse = any(Response.class);

        // When "mocked behaviour"
        when(orderService.placeOrder(orderRequest)).thenReturn(expectedResponse);

        String expectedResponseJson = objectMapper.writeValueAsString(expectedResponse);

        // and "endpoint hit"
        mockMvc.perform(MockMvcRequestBuilders.post("/kiwi/api/v1/orders/place")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk());

        // Then "verify"
        verify(orderService, times(1)).placeOrder(orderRequest);
    }
}