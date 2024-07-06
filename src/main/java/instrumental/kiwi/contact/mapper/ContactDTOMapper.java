package instrumental.kiwi.contact.mapper;

import instrumental.kiwi.contact.dto.ContactDTO;
import instrumental.kiwi.contact.model.Contact;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ContactDTOMapper implements Function<Contact, ContactDTO> {
    @Override
    public ContactDTO apply(Contact contact) {
        return new ContactDTO(
                contact.getContactId()
        );
    }
}