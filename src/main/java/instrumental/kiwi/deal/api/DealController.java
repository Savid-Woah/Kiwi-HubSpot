package instrumental.kiwi.deal.api;

import instrumental.kiwi.deal.service.DealService;
import instrumental.kiwi.response.annotation.ApiResponder;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Deal")
@RequiredArgsConstructor
@RequestMapping("kiwi/api/v1/deals/")
public class DealController {

    private final DealService dealService;

    @ApiResponder
    @GetMapping(path = "get-by-store/{store-id}/{page-size}")
    public String getAllDealsByStore(
            @PathVariable("store-id") Long storeId,
            @PathVariable("page-size") Integer pageSize
    ) {
        return dealService.getAllDealsByStore(storeId, pageSize);
    }
}