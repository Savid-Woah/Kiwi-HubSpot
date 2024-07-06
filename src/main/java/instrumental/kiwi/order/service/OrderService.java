package instrumental.kiwi.order.service;

import instrumental.kiwi.customer.model.Customer;
import instrumental.kiwi.customer.repository.CustomerRepository;
import instrumental.kiwi.deal.service.DealService;
import instrumental.kiwi.email.EmailService;
import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.hubspot.provider.line_item.HubSpotLineItemProvider;
import instrumental.kiwi.hubspot.provider.order.HubSpotOrderProvider;
import instrumental.kiwi.line_item.request.*;
import instrumental.kiwi.order.dto.OrderDTO;
import instrumental.kiwi.order.mapper.OrderDTOMapper;
import instrumental.kiwi.order.mapper.OrderHubSpotMapper;
import instrumental.kiwi.order.model.Order;
import instrumental.kiwi.order.repository.OrderRepository;
import instrumental.kiwi.order.request.OrderRequest;
import instrumental.kiwi.product.model.Product;
import instrumental.kiwi.product.service.ProductService;
import instrumental.kiwi.stock.service.StockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static instrumental.kiwi.exception.MsgCode.CUSTOMER_NOT_FOUND;
import static instrumental.kiwi.hubspot.constant.AssociationCategory.HUBSPOT_DEFINED;
import static instrumental.kiwi.hubspot.constant.AssociationTypeId.ORDER_TO_LINE_ITEM;
import static instrumental.kiwi.response.handler.ResponseHandler.generateResponse;
import static instrumental.kiwi.response.message.ResponseMessage.ORDER_ADDED;
import static org.springframework.http.HttpStatus.CREATED;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final DealService dealService;
    private final EmailService emailService;
    private final StockService stockService;
    private final ProductService productService;
    private final OrderDTOMapper orderDTOMapper;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderHubSpotMapper orderHubSpotMapper;
    private final HubSpotOrderProvider hubSpotOrderProvider;
    private final HubSpotLineItemProvider hubSpotLineItemProvider;

    public Map<String, Object> placeOrder(OrderRequest orderRequest) {

        List<AssociationRequest> associations = handleOrderLineItems(orderRequest);
        String createdHubspotOrder = hubSpotOrderProvider.postOrder(orderRequest, associations);
        Order order = orderHubSpotMapper.apply(createdHubspotOrder);
        setOrderComplements(orderRequest, order);

        Order savedOrder = orderRepository.save(order);
        OrderDTO orderDTO = orderDTOMapper.apply(savedOrder);

        dealService.createDealForOrder(order);
        emailService.sendBillToCustomer(order);

        return generateResponse(orderDTO, CREATED, ORDER_ADDED);
    }

    private void setOrderComplements(OrderRequest orderRequest, Order order) {

        Long customerId = orderRequest.getCustomerId();
        Double totalPrice = calculateTotalPrice(orderRequest);

        Customer customer = customerRepository.findById(customerId).orElseThrow(()
                -> new BackendException(CUSTOMER_NOT_FOUND));

        order.setCustomer(customer);
        order.setTotalPrice(totalPrice);
    }

    private List<AssociationRequest> handleOrderLineItems(OrderRequest orderRequest) {

        List<AssociationRequest> associations = new ArrayList<>();
        List<ItemPurchased> itemsPurchased = orderRequest.getItemsPurchased();

        for(ItemPurchased itemPurchased: itemsPurchased) {
            handleItemPurchased(itemPurchased, associations);
        }

        return associations;
    }

    private void handleItemPurchased(ItemPurchased itemPurchased, List<AssociationRequest> associations) {

        Product product = productService.getProductById(itemPurchased.getProductId());
        LineItemRequest lineItemRequest = buildLineItemRequest(itemPurchased, product);

        String createdLineItemId = handleHubSpotLineItems(lineItemRequest);
        AssociationRequest associationRequest = buildAssociationRequest(createdLineItemId);

        stockService.lessenProductStock(product, lineItemRequest.getQuantity());
        associations.add(associationRequest);
    }

    private String handleHubSpotLineItems(LineItemRequest lineItemRequest) {

        String createdLineItem = hubSpotLineItemProvider.postLineItem(lineItemRequest);
        return hubSpotLineItemProvider.extractLineItemId(createdLineItem);
    }

    private LineItemRequest buildLineItemRequest(ItemPurchased itemPurchased, Product product) {

        Integer quantity = itemPurchased.getQuantity();
        return new LineItemRequest(product, quantity);
    }

    private AssociationRequest buildAssociationRequest(String createdLineItemId) {

        AssociationTo associationTo = new AssociationTo(createdLineItemId);
        AssociationType associationType = new AssociationType(HUBSPOT_DEFINED, ORDER_TO_LINE_ITEM);
        List<AssociationType> associationTypes = new ArrayList<>(List.of(associationType));
        return new AssociationRequest(associationTo, associationTypes);
    }

    private Double calculateTotalPrice(OrderRequest orderRequest) {

        double totalPrice = 0.0;

        for (ItemPurchased itemPurchased : orderRequest.getItemsPurchased()) {

            Product product = productService.getProductById(itemPurchased.getProductId());
            Double productPrice = product.getPrice();
            double itemTotal = productPrice * itemPurchased.getQuantity();
            totalPrice += itemTotal;
        }

        return totalPrice;
    }
}
