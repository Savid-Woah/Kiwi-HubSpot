package instrumental.kiwi.ticket.model;

import instrumental.kiwi.order.model.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Ticket")
@Table(name = "tickets")
public class Ticket {

    // Get tickets from customer and claim supplier
    @Id
    @Column(name = "ticket_id", updatable = false, nullable = false)
    private Long ticketId;

    @Column(name = "hs_pipeline", nullable = false)
    private String hsPipeline;

    @Column(name = "hs_pipeline_stage", nullable = false)
    private String hsPipelineStage;

    @Column(name = "hs_ticket_priority", nullable = false)
    private String hsTicketPriority;

    @Column(name = "subject")
    private String subject;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", updatable = false, nullable = false)
    private Order order;

}
