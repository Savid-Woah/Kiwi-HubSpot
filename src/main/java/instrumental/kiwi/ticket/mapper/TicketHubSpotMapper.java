package instrumental.kiwi.ticket.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.ticket.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Function;

import static instrumental.kiwi.hubspot.param.HubSpotOrderParam.ID;
import static instrumental.kiwi.hubspot.param.HubSpotOrderParam.PROPERTIES;
import static instrumental.kiwi.hubspot.param.HubSpotTicketParam.*;

@Component
@RequiredArgsConstructor
public class TicketHubSpotMapper implements Function<String, Ticket> {

    private final ObjectMapper objectMapper;

    @Override
    public Ticket apply(String createdTicketJsonString) {

        try {

            JsonNode rootNode = objectMapper.readTree(createdTicketJsonString);

            Ticket ticket = new Ticket();
            ticket.setTicketId(rootNode.path(ID).asLong());
            JsonNode propertiesNode = rootNode.path(PROPERTIES);
            ticket.setHsTicketPriority(propertiesNode.path(HS_TICKET_PRIORITY).asText());
            ticket.setHsPipelineStage(propertiesNode.path(HS_PIPELINE_STAGE).asText());
            ticket.setHsPipeline(propertiesNode.path(HS_PIPELINE).asText());
            ticket.setSubject(propertiesNode.path(SUBJECT).asText());
            return ticket;

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON string", e);
        }
    }
}