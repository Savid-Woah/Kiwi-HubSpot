package instrumental.kiwi.ticket.api;

import instrumental.kiwi.response.Response;
import instrumental.kiwi.response.annotation.ApiResponder;
import instrumental.kiwi.security.annotation.WithRateLimitProtection;
import instrumental.kiwi.ticket.request.TicketRequest;
import instrumental.kiwi.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Ticket")
@RequiredArgsConstructor
@RequestMapping("kiwi/api/v1/tickets/")
public class TicketController {

    private final TicketService ticketService;

    @ApiResponder
    @WithRateLimitProtection
    @GetMapping(path = "get-by-order/{order-id}/{page-size}")
    public String getTickerByOrder(
            @PathVariable("order-id") Long orderId,
            @PathVariable("page-size") Integer pageSize
    ) {
        return ticketService.getTicketByOrder(orderId, pageSize);
    }

    @ApiResponder
    @WithRateLimitProtection
    @PostMapping(path = "add")
    public Response addTicket(@Validated @RequestBody TicketRequest ticketRequest) {
        return ticketService.addTicket(ticketRequest);
    }
}