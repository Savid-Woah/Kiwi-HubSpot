package instrumental.kiwi.ticket.repository;

import instrumental.kiwi.ticket.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}