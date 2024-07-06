package instrumental.kiwi.ticket.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.exception.MessageTextResolver;
import instrumental.kiwi.security.auth.service.AuthService;
import instrumental.kiwi.security.config.filter.JwtAuthenticationFilter;
import instrumental.kiwi.security.config.service.JwtService;
import instrumental.kiwi.security.config.service.OAuth2AuthenticationSuccessHandler;
import instrumental.kiwi.ticket.request.TicketRequest;
import instrumental.kiwi.ticket.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static instrumental.kiwi.exception.MsgCode.ORDER_NOT_FOUND;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;
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
    private TicketService ticketService;
    @Autowired
    private ObjectMapper objectMapper;
    private TicketRequest ticketRequest;
    private Map<String, Object> responseMap;

    @BeforeEach
    void setUp() {

        ticketRequest = new TicketRequest();
        ticketRequest.setOrderId(1L);
        ticketRequest.setQuantity(1);
        ticketRequest.setProductId(1L);
        ticketRequest.setHsPipeline("0");
        ticketRequest.setHsPipelineStage("1");
        ticketRequest.setHsTicketPriority("HIGH");
        ticketRequest.setSubject("Product damaged");
    }

    @Test
    @DisplayName(value = "Should get ticket by order")
    void shouldGetTicketByOrder() throws Exception {

        //When "service called"
        when(ticketService.getTicketByOrder(anyLong(), anyInt()))
                .thenReturn("{}");

        // and "endpoint hit"
        mockMvc.perform(get("/kiwi/api/v1/tickets/get-by-order/1/10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName(value = "Should add ticket")
    void shouldAddTicket() throws Exception {

        //When "service called"
        when(ticketService.addTicket(ticketRequest)).thenReturn(responseMap);

        // and "endpoint hit"
        mockMvc.perform(post("/kiwi/api/v1/tickets/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName(value = "Should throw exception when order not found")
    void shouldThrowBackendExceptionWhenOrderNotFound() throws Exception {

        //When "service called"
        when(ticketService.addTicket(ticketRequest)).thenThrow(new BackendException(ORDER_NOT_FOUND));

        // and "endpoint hit"
        mockMvc.perform(post("/kiwi/api/v1/tickets/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketRequest)))
                .andExpect(status().isInternalServerError());
    }
}
