package instrumental.kiwi.ticket.mapper;

import instrumental.kiwi.ticket.dto.TicketDTO;
import instrumental.kiwi.ticket.model.Ticket;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TicketDTOMapper implements Function<Ticket, TicketDTO> {
    @Override
    public TicketDTO apply(Ticket ticket) {
        return new TicketDTO(
                ticket.getTicketId()
        );
    }
}