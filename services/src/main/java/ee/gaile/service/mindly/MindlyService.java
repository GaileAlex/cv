package ee.gaile.service.mindly;

import ee.gaile.models.mindly.Mindly;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Aleksei Gaile 19-Sep-21
 */
public interface MindlyService {

    /**
     * Gives a list of currencies to the cryptoportfolio
     *
     * @return - list of currencies cryptoportfolio
     */
    List<Mindly> getAllPortfolio();

    /**
     * Save the new entry to the cryptoportfolio
     *
     * @param portfolio currencies cryptoportfolio
     * @return - cryptoportfolio
     */
    Mindly savePortfolio(@RequestBody Mindly portfolio);

    /**
     * Deletes the entry in the cryptoportfolio
     *
     * @param portfolioId - portfolio ID
     */
    void deletePortfolioById(Long portfolioId);
}
