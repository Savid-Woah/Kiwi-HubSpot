package instrumental.kiwi.ticket.service;

import instrumental.kiwi.email.EmailService;
import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchProvider;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchRequest;
import instrumental.kiwi.hubspot.provider.ticket.HubSpotTicketProvider;
import instrumental.kiwi.order.model.Order;
import instrumental.kiwi.order.repository.OrderRepository;
import instrumental.kiwi.product.model.Product;
import instrumental.kiwi.product.repository.ProductRepository;
import instrumental.kiwi.ticket.dto.TicketDTO;
import instrumental.kiwi.ticket.mapper.TicketDTOMapper;
import instrumental.kiwi.ticket.mapper.TicketHubSpotMapper;
import instrumental.kiwi.ticket.model.Ticket;
import instrumental.kiwi.ticket.repository.TicketRepository;
import instrumental.kiwi.ticket.request.TicketRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static instrumental.kiwi.hubspot.provider.search.HubSpotSearchRequest.buildForTicketByOrder;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private EmailService emailService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private TicketDTOMapper ticketDTOMapper;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private TicketHubSpotMapper ticketHubSpotMapper;
    @Mock
    private HubSpotSearchProvider hubSpotSearchProvider;
    @Mock
    private HubSpotTicketProvider hubSpotTicketProvider;

    @InjectMocks
    private TicketService ticketService;

    private TicketRequest ticketRequest;
    private Ticket ticket;
    private Order order;
    private Product product;

    @BeforeEach
    void setUp() {

        order = new Order();
        order.setOrderId(1L);

        product = new Product();
        product.setProductId(1L);
        product.setName("Product Name");

        ticketRequest = new TicketRequest();
        ticketRequest.setOrderId(1L);
        ticketRequest.setProductId(1L);
        ticketRequest.setQuantity(2);

        ticket = new Ticket();
        ticket.setOrder(order);
    }

    @Test
    void shouldGetTicketByOrder() {

        // Given
        Long orderId = 1L;
        Integer pageSize = 10;
        HubSpotSearchRequest searchRequest = buildForTicketByOrder(orderId, pageSize);

        // When
        when(hubSpotSearchProvider.search(any(HubSpotSearchRequest.class))).thenReturn("searchResult");
        String result = ticketService.getTicketByOrder(orderId, pageSize);

        // Then
        assertNotNull(result);
        verify(hubSpotSearchProvider, times(1)).search(searchRequest);
    }

    @Test
    void shouldAddTicket() {

        // When
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(hubSpotTicketProvider.postTicket(any(TicketRequest.class), anyList())).thenReturn("{}");
        when(ticketHubSpotMapper.apply(anyString())).thenReturn(ticket);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(ticketDTOMapper.apply(any(Ticket.class))).thenReturn(new TicketDTO(0L));

        Map<String, Object> response = ticketService.addTicket(ticketRequest);

        // Then
        assertNotNull(response);
        verify(orderRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(hubSpotTicketProvider, times(1)).postTicket(any(TicketRequest.class), anyList());
        verify(ticketRepository, times(1)).save(ticket);
        verify(emailService, times(1)).requestProductReplaceToSupplier(product, 2);
    }

    @Test
    void shouldThrowBackendExceptionWhenOrderNotFound() {

        // When
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        BackendException exception = assertThrows(BackendException.class,
                () -> ticketService.addTicket(ticketRequest));

        // Then
        assertNotNull(exception);
        verify(orderRepository, times(1)).findById(1L);
        verify(ticketRepository, never()).save(any(Ticket.class));
    }
}
