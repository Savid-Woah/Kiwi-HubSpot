package instrumental.kiwi.contact.service;

import instrumental.kiwi.contact.dto.ContactDTO;
import instrumental.kiwi.contact.mapper.ContactHubSpotMapper;
import instrumental.kiwi.contact.model.Contact;
import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.hubspot.provider.contact.HubSpotContactProvider;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchProvider;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchRequest;
import instrumental.kiwi.store.model.Store;
import instrumental.kiwi.store.repository.StoreRepository;
import instrumental.kiwi.contact.mapper.ContactDTOMapper;
import instrumental.kiwi.contact.repository.ContactRepository;
import instrumental.kiwi.contact.request.ContactRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

import static instrumental.kiwi.exception.MsgCode.STORE_NOT_FOUND;
import static instrumental.kiwi.hubspot.provider.search.HubSpotSearchRequest.buildForContactsByStore;
import static instrumental.kiwi.response.handler.ResponseHandler.generateResponse;
import static instrumental.kiwi.response.message.ResponseMessage.CONTACT_ADDED;
import static org.springframework.http.HttpStatus.CREATED;

@Service
@Transactional
@RequiredArgsConstructor
public class ContactService {

    private final StoreRepository storeRepository;
    private final ContactDTOMapper contactDTOMapper;
    private final ContactRepository contactRepository;
    private final ContactHubSpotMapper contactHubSpotMapper;
    private final HubSpotSearchProvider hubSpotSearchProvider;
    private final HubSpotContactProvider hubSpotContactProvider;

    public Map<String, Object> addContact(ContactRequest contactRequest) {

        String createdContact = hubSpotContactProvider.postContact(contactRequest);
        Contact contact = contactHubSpotMapper.apply(createdContact);
        setContactRelations(contactRequest, contact);
        Contact savedContact = contactRepository.save(contact);
        ContactDTO contactDTO = contactDTOMapper.apply(savedContact);

        return generateResponse(contactDTO, CREATED, CONTACT_ADDED);
    }

    private void setContactRelations(ContactRequest contactRequest, Contact contact) {

        Long storeId = contactRequest.getStoreId();

        Store store = storeRepository.findById(storeId).orElseThrow(()
                -> new BackendException(STORE_NOT_FOUND));

        contact.setStore(store);
    }

    public String getAllContactsByStore(Long storeId, Integer pageSize) {

        HubSpotSearchRequest searchRequest = buildForContactsByStore(storeId, pageSize);
        return hubSpotSearchProvider.search(searchRequest);
    }
}