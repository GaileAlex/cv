package ee.gaile.controller.mindly;

import ee.gaile.entity.mindly.Mindly;
import ee.gaile.service.mindly.MindlyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

@Slf4j
@RestController
@RequestMapping(path = API_V1_PREFIX + "/mindly-data")
@AllArgsConstructor
public class MindlyController {
    private final MindlyService mindlyService;

    @GetMapping
    public ResponseEntity<List<Mindly>> getPortfolio() {
        return new ResponseEntity<>(mindlyService.getAllPortfolio(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Mindly> addPortfolioItem(@RequestBody Mindly portfolio) {
        return new ResponseEntity<>(mindlyService.savePortfolio(portfolio), HttpStatus.OK);
    }

    @DeleteMapping("/{portfolioId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePortfolioItem(@PathVariable("portfolioId") Long portfolioId) {
        mindlyService.deletePortfolioById(portfolioId);
    }
}
