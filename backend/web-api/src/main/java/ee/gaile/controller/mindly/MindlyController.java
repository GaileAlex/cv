package ee.gaile.controller.mindly;

import ee.gaile.repository.entity.mindly.Portfolio;
import ee.gaile.repository.mindly.PortfolioRepository;
import ee.gaile.service.mindly.BitfinexAccess;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/mindly-data",
        produces = "application/json")
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@AllArgsConstructor
public class MindlyController {
    private final PortfolioRepository portfolioRepository;
    private final BitfinexAccess bitfinexAccess;

    /**
     * getting data from the database, calculating the value of the currency
     *
     * @return List<Portfolio>
     * @throws IOException
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Portfolio>> getPortfolio() throws IOException {
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        for (Portfolio portfolio : portfolioList) {
            portfolio.setCurrentMarketValue(portfolio.getAmount()
                    .multiply(bitfinexAccess.getCurrency(portfolio.getCryptocurrency())));
        }

        return new ResponseEntity<>(portfolioList, HttpStatus.OK);
    }

    /**
     * saving object to database
     *
     * @param portfolio get object
     * @return save to DB
     */
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Portfolio addPortfolioItem(@RequestBody Portfolio portfolio) {
        if (portfolio.getDateOfPurchase() == null) {
            portfolio.setDateOfPurchase(new Date());
        }

        return portfolioRepository.save(portfolio);
    }

    /**
     * @param deleteItem id portfolio
     */
    @DeleteMapping("/{deleteItem}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID deleteItem) {
        try {
            portfolioRepository.deleteById(deleteItem);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
    }
}
