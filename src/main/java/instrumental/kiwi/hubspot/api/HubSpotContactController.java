package instrumental.kiwi.hubspot.api;

import instrumental.kiwi.contact.request.ContactRequest;
import instrumental.kiwi.hubspot.provider.contact.HubSpotContactProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("hubspot-buddy/api/v1/contacts/")
public class HubSpotContactController {

    private final HubSpotContactProvider hubSpotContactProvider;

    @PostMapping
    public String postContact(@Validated @RequestBody ContactRequest contactRequest) {
        return hubSpotContactProvider.postContact(contactRequest);
    }
}