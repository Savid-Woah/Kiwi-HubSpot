package instrumental.kiwi.ticket.service;

import instrumental.kiwi.email.EmailService;
import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchProvider;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchRequest;
import instrumental.kiwi.hubspot.provider.ticket.HubSpotTicketProvider;
import instrumental.kiwi.line_item.request.AssociationRequest;
import instrumental.kiwi.line_item.request.AssociationTo;
import instrumental.kiwi.line_item.request.AssociationType;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static instrumental.kiwi.exception.MsgCode.*;
import static instrumental.kiwi.hubspot.constant.AssociationCategory.HUBSPOT_DEFINED;
import static instrumental.kiwi.hubspot.constant.AssociationTypeId.TICKET_TO_ORDER;
import static instrumental.kiwi.hubspot.provider.search.HubSpotSearchRequest.buildForTicketByOrder;
import static instrumental.kiwi.response.handler.ResponseHandler.generateResponse;
import static instrumental.kiwi.response.message.ResponseMessage.TICKET_ADDED;
import static org.springframework.http.HttpStatus.CREATED;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketService {

    private final EmailService emailService;
    private final OrderRepository orderRepository;
    private final TicketDTOMapper ticketDTOMapper;
    private final TicketRepository ticketRepository;
    private final ProductRepository productRepository;
    private final TicketHubSpotMapper ticketHubSpotMapper;
    private final HubSpotSearchProvider hubSpotSearchProvider;
    private final HubSpotTicketProvider hubSpotTicketProvider;

    public String getTicketByOrder(Long orderId, Integer pageSize) {

        HubSpotSearchRequest searchRequest = buildForTicketByOrder(orderId, pageSize);
        return hubSpotSearchProvider.search(searchRequest);
    }

    public Map<String, Object> addTicket(TicketRequest ticketRequest) {

        String orderId = ticketRequest.getOrderId().toString();
        AssociationRequest associationRequest = buildAssociationRequest(orderId);
        List<AssociationRequest> associations = new ArrayList<>(List.of(associationRequest));
        String createdTicket = hubSpotTicketProvider.postTicket(ticketRequest, associations);
        Ticket ticket = ticketHubSpotMapper.apply(createdTicket);
        setTicketRelations(ticketRequest, ticket);

        Ticket savedTicket = ticketRepository.save(ticket);
        TicketDTO ticketDTO = ticketDTOMapper.apply(savedTicket);

        submitTicketClaimToSupplier(ticketRequest);

        return generateResponse(ticketDTO, CREATED, TICKET_ADDED);
    }

    private void setTicketRelations(TicketRequest ticketRequest, Ticket ticket) {

        Long orderId = ticketRequest.getOrderId();
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new BackendException(ORDER_NOT_FOUND));

        ticket.setOrder(order);
    }

    private AssociationRequest buildAssociationRequest(String orderId) {

        AssociationTo associationTo = new AssociationTo(orderId);
        AssociationType associationType = new AssociationType(HUBSPOT_DEFINED, TICKET_TO_ORDER);
        List<AssociationType> associationTypes = new ArrayList<>(List.of(associationType));
        return new AssociationRequest(associationTo, associationTypes);
    }

    private void submitTicketClaimToSupplier(TicketRequest ticketRequest) {

        Long productId = ticketRequest.getProductId();
        Integer quantity = ticketRequest.getQuantity();

        Product product = productRepository.findById(productId).orElseThrow(()
                -> new BackendException(PRODUCT_NOT_FOUND));

        emailService.requestProductReplaceToSupplier(product, quantity);
    }
}