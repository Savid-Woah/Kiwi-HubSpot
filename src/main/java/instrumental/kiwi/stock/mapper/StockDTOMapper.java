package instrumental.kiwi.stock.mapper;

import instrumental.kiwi.stock.dto.StockDTO;
import instrumental.kiwi.stock.model.Stock;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StockDTOMapper implements Function<Stock, StockDTO> {

    @Override
    public StockDTO apply(Stock stock) {

        return new StockDTO(

                stock.getStockId()
        );
    }
}