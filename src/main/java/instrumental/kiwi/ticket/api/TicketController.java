package instrumental.kiwi.ticket.api;

import instrumental.kiwi.security.annotation.WithRateLimitProtection;
import instrumental.kiwi.ticket.request.TicketRequest;
import instrumental.kiwi.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("kiwi/api/v1/tickets/")
public class TicketController {

    private final TicketService ticketService;

    @WithRateLimitProtection
    @GetMapping(path = "get-by-order/{order-id}/{page-size}")
    public String getTickerByOrder(
            @PathVariable("order-id") Long orderId,
            @PathVariable("page-size") Integer pageSize
    ) {
        return ticketService.getTicketByOrder(orderId, pageSize);
    }

    @WithRateLimitProtection
    @PostMapping(path = "add")
    public Map<String, Object> addTicket(@Validated @RequestBody TicketRequest ticketRequest) {
        return ticketService.addTicket(ticketRequest);
    }
}