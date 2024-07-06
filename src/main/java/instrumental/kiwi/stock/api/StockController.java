package instrumental.kiwi.stock.api;

import instrumental.kiwi.security.annotation.WithRateLimitProtection;
import instrumental.kiwi.stock.dto.StockDTO;
import instrumental.kiwi.stock.request.StockRefillRequest;
import instrumental.kiwi.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("kiwi/api/v1/stock/")
public class StockController {

    private final StockService stockService;

    @WithRateLimitProtection
    @GetMapping(path = "get-by-product/{product-id}")
    public Optional<StockDTO> getStockByProduct(@PathVariable("product-id") Long productId) {
        return stockService.getStockByProduct(productId);
    }

    @WithRateLimitProtection
    @PatchMapping(path = "refill")
    public void refillProductStock(@Validated @RequestBody StockRefillRequest stockRefillRequest) {
        stockService.refillProductStock(stockRefillRequest);
    }
}