package ee.gaile.controller.mindly;

import ee.gaile.entity.mindly.Mindly;
import ee.gaile.service.mindly.MindlyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

@Slf4j
@RestController
@RequestMapping(path = API_V1_PREFIX + "/mindly-data")
@AllArgsConstructor
public class MindlyController {
    private final MindlyService mindlyService;

    /**
     * getting data from the database, calculating the value of the currency
     *
     * @return List<Mindly>
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<Mindly> getPortfolio() {
        return mindlyService.getAllPortfolio();
    }

    /**
     * saving object to database
     *
     * @param portfolio get object
     * @return save to DB
     */
    @PostMapping(consumes = "application/json")
    public Mindly addPortfolioItem(@RequestBody Mindly portfolio) {
        return mindlyService.savePortfolio(portfolio);
    }

    /**
     * @param portfolioId id portfolio item
     */
    @DeleteMapping("/{portfolioId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePortfolioItem(@PathVariable("portfolioId") Long portfolioId) {
        mindlyService.deletePortfolioById(portfolioId);
    }
}
