package instrumental.kiwi.hubspot.api;

import instrumental.kiwi.hubspot.provider.company.HubSpotCompanyProvider;
import instrumental.kiwi.store.request.StoreRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("hubspot-buddy/api/v1/companies/")
public class HubSpotCompanyController {

    private final HubSpotCompanyProvider hubSpotCompanyProvider;

    @PostMapping
    public String postCompany(@Validated @RequestBody StoreRequest storeRequest) {
        return hubSpotCompanyProvider.postCompany(storeRequest);
    }
}