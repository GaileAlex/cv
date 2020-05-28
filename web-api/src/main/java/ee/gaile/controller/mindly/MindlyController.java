package ee.gaile.controller.mindly;

import ee.gaile.repository.entity.mindly.Portfolio;
import ee.gaile.service.RepositoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/mindly-data",
        produces = "application/json")
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@AllArgsConstructor
public class MindlyController {
    private final RepositoryService repositoryService;

    /**
     * getting data from the database, calculating the value of the currency
     *
     * @return List<Portfolio>
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<Portfolio> getPortfolio() {

        return repositoryService.getAllPortfolio();
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

        return repositoryService.savePortfolio(portfolio);
    }

    /**
     * @param deleteItem id portfolio
     */
    @DeleteMapping("/{deleteItem}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long deleteItem) {
        try {
            repositoryService.deletePortfolioById(deleteItem);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
    }
}
