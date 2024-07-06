package instrumental.kiwi.hubspot.api;

import instrumental.kiwi.hubspot.provider.search.HubSpotSearchProvider;
import instrumental.kiwi.hubspot.provider.search.HubSpotSearchRequest;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("hubspot-buddy/api/v1/search/")
public class HubSpotSearchController {

    private final HubSpotSearchProvider hubSpotSearchProvider;

    @PostMapping(path = "product")
    public String searchProduct(@Validated @RequestBody HubSpotSearchRequest searchRequest) {
        return hubSpotSearchProvider.search(searchRequest);
    }
}