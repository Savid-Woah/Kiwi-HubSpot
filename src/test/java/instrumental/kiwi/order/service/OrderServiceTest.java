package instrumental.kiwi.order.service;

import instrumental.kiwi.customer.model.Customer;
import instrumental.kiwi.line_item.request.LineItemRequest;
import instrumental.kiwi.order.mapper.OrderDTOMapper;
import instrumental.kiwi.order.mapper.OrderHubSpotMapper;
import instrumental.kiwi.order.model.Order;
import instrumental.kiwi.order.repository.OrderRepository;
import instrumental.kiwi.order.request.OrderRequest;
import instrumental.kiwi.product.model.Product;
import instrumental.kiwi.product.service.ProductService;
import instrumental.kiwi.line_item.request.ItemPurchased;
import instrumental.kiwi.hubspot.provider.line_item.HubSpotLineItemProvider;
import instrumental.kiwi.hubspot.provider.order.HubSpotOrderProvider;
import instrumental.kiwi.customer.repository.CustomerRepository;
import instrumental.kiwi.deal.service.DealService;
import instrumental.kiwi.email.EmailService;
import instrumental.kiwi.stock.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private DealService dealService;
    @Mock
    private EmailService emailService;
    @Mock
    private StockService stockService;
    @Mock
    private ProductService productService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private OrderHubSpotMapper orderHubSpotMapper;
    @Mock
    private HubSpotOrderProvider hubSpotOrderProvider;
    @Mock
    private HubSpotLineItemProvider hubSpotLineItemProvider;
    @Spy
    private OrderDTOMapper orderDTOMapper = new OrderDTOMapper();
    @InjectMocks
    private OrderService orderService;
    private Order order;
    private Product product;
    private Customer customer;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setCustomerId(1L);

        product = new Product();
        product.setProductId(1L);
        product.setPrice(100.0);
        product.setName("Product Name");

        ItemPurchased itemPurchased = new ItemPurchased();
        itemPurchased.setProductId(1L);
        itemPurchased.setQuantity(2);

        orderRequest = new OrderRequest();
        orderRequest.setCustomerId(1L);
        orderRequest.setItemsPurchased(List.of(itemPurchased));

        order = new Order();
        order.setOrderId(1L);
    }

    @Test
    @DisplayName(value = "Should place order")
    void shouldPlaceOrder() {

        // When "mocked behaviour"
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productService.getProductById(1L)).thenReturn(product);
        when(hubSpotLineItemProvider.postLineItem(any(LineItemRequest.class))).thenReturn("lineItemId");
        when(hubSpotLineItemProvider.extractLineItemId("lineItemId")).thenReturn("extractedLineItemId");
        when(hubSpotOrderProvider.postOrder(any(OrderRequest.class), anyList())).thenReturn("{}");
        when(orderHubSpotMapper.apply("{}")).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);  // Ensure save returns the order

        // "and service called"
        orderService.placeOrder(orderRequest);

        // Then "verify"
        verify(customerRepository, times(1)).findById(1L);
        verify(productService, times(2)).getProductById(1L);
        verify(hubSpotLineItemProvider, times(1)).postLineItem(any(LineItemRequest.class));
        verify(hubSpotLineItemProvider, times(1)).extractLineItemId(anyString());
        verify(hubSpotOrderProvider, times(1)).postOrder(any(OrderRequest.class), anyList());
        verify(orderRepository, times(1)).save(order);
        verify(dealService, times(1)).createDealForOrder(order);
        verify(emailService, times(1)).sendBillToCustomer(order);
    }
}
