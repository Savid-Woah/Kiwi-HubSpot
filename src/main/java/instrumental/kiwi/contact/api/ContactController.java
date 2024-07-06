package instrumental.kiwi.contact.api;

import instrumental.kiwi.contact.request.ContactRequest;
import instrumental.kiwi.contact.service.ContactService;
import instrumental.kiwi.response.Response;
import instrumental.kiwi.response.annotation.ApiResponder;
import instrumental.kiwi.security.annotation.WithRateLimitProtection;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Contact")
@RequiredArgsConstructor
@RequestMapping("kiwi/api/v1/contacts/")
public class ContactController {

    private final ContactService contactService;

    @ApiResponder
    @WithRateLimitProtection
    @PostMapping(path = "add")
    public Response addContact(@Validated @RequestBody ContactRequest contactRequest) {
        return contactService.addContact(contactRequest);
    }

    @ApiResponder
    @WithRateLimitProtection
    @GetMapping(path = "get-by-store/{store-id}/{page-size}")
    public String getAllContactsByStore(
            @PathVariable("store-id") Long storeId,
            @PathVariable("page-size") Integer pageSize
    ) {
        return contactService.getAllContactsByStore(storeId, pageSize);
    }
}