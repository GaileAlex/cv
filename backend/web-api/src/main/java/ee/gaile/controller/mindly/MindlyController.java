package ee.gaile.controller.mindly;

import ee.gaile.repository.entity.mindly.Portfolio;
import ee.gaile.service.BitfinexAccessService;
import ee.gaile.service.RepositoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
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

    private final BitfinexAccessService bitfinexAccessService;
    private RepositoryService repositoryService;

    /**
     * getting data from the database, calculating the value of the currency
     *
     * @return List<Portfolio>
     * @throws IOException
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<Portfolio> getPortfolio() throws IOException {
        List<Portfolio> portfolioList = repositoryService.getAllPortfolio();
        for (Portfolio portfolio : portfolioList) {
            portfolio.setCurrentMarketValue(portfolio.getAmount()
                    .multiply(bitfinexAccessService.getCurrency(portfolio.getCryptocurrency())));
        }

        return portfolioList;
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

        return repositoryService.savePortfolio(portfolio);
    }

    /**
     * @param deleteItem id portfolio
     */
    @DeleteMapping("/{deleteItem}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID deleteItem) {
        try {
            repositoryService.deletePortfolioById(deleteItem);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
    }
}
