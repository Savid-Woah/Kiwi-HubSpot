package instrumental.kiwi.contact.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import instrumental.kiwi.contact.enums.ContactType;
import instrumental.kiwi.contact.model.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static instrumental.kiwi.hubspot.param.HubSpotContactParam.*;
import static instrumental.kiwi.hubspot.param.HubSpotOrderParam.PROPERTIES;
import static instrumental.kiwi.hubspot.param.HubSpotProductParam.ID;

@Component
@RequiredArgsConstructor
public class ContactHubSpotMapper implements Function<String, Contact> {

    private final ObjectMapper objectMapper;

    @Override
    public Contact apply(String createdContactJsonString) {
        try {

            Contact contact = new Contact();

            JsonNode jsonNode = objectMapper.readTree(createdContactJsonString);

            JsonNode properties = jsonNode.get(PROPERTIES);
            contact.setContactId(jsonNode.get(ID).asLong());

            contact.setLifecyclestage(properties.get(LIFECYCLESTAGE).asText());
            contact.setFirstname(properties.get(FIRSTNAME).asText());
            contact.setLastname(properties.get(LASTNAME).asText());
            contact.setCompany(properties.get(COMPANY).asText());
            contact.setWebsite(properties.get(WEBSITE).asText());
            contact.setEmail(properties.get(EMAIL).asText());
            contact.setPhone(properties.get(PHONE).asText());
            contact.setType(ContactType.SUPPLIER);

            return contact;

        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}