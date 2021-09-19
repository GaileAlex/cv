package ee.gaile.controller.mindly;

import ee.gaile.models.mindly.Mindly;
import ee.gaile.service.mindly.MindlyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

/**
 * REST service controller for retrieving, deleting and adding data to the tables of the crypto portfolio test task
 *
 * @author Aleksei Gaile
 */
@RestController
@RequestMapping(path = API_V1_PREFIX + "/mindly-data")
@RequiredArgsConstructor
@Tag(name = "MindlyController", description = "Controller for working with mindlyService")
public class MindlyController {
    private final MindlyService mindlyService;

    @GetMapping
    @Operation(summary = "Portfolio display service")
    public ResponseEntity<List<Mindly>> getPortfolio() {
        return new ResponseEntity<>(mindlyService.getAllPortfolio(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Portfolio adding service")
    public ResponseEntity<Mindly> addPortfolioItem(@RequestBody Mindly portfolio) {
        return new ResponseEntity<>(mindlyService.savePortfolio(portfolio), HttpStatus.OK);
    }

    @DeleteMapping("/{portfolioId}")
    @Operation(summary = "Portfolio deletion service")
    public void deletePortfolioItem(@PathVariable("portfolioId") Long portfolioId) {
        mindlyService.deletePortfolioById(portfolioId);
    }
}
