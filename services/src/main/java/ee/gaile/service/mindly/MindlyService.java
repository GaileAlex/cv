package ee.gaile.service.mindly;

import ee.gaile.entity.mindly.Mindly;
import ee.gaile.repository.mindly.CryptocurrencyValueRepository;
import ee.gaile.repository.mindly.MindlyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for working with test task data Mindly
 *
 * @author Aleksei Gaile
 */
@Service
@Transactional
@AllArgsConstructor
public class MindlyService {
    private final MindlyRepository mindlyRepository;
    private final CryptocurrencyValueRepository cryptocurrencyValueRepository;

    /**
     * Gives a list of currencies to the cryptoportfolio
     *
     * @return - list of currencies cryptoportfolio
     */
    public List<Mindly> getAllPortfolio() {
        List<Mindly> portfolioList = mindlyRepository.findAll();

        portfolioList.forEach((portfolio) -> {
            BigDecimal value = cryptocurrencyValueRepository
                    .findByCryptocurrencyNameAndLastDate(portfolio.getCryptocurrency()).getValueCurrency()
                    .setScale(2, BigDecimal.ROUND_HALF_EVEN);

            portfolio.setCurrentMarketValue(portfolio.getAmount()
                    .multiply(value));
        });

        return portfolioList;
    }

    /**
     * Save the new entry to the cryptoportfolio
     *
     * @param portfolio currencies cryptoportfolio
     * @return - cryptoportfolio
     */
    public Mindly savePortfolio(Mindly portfolio) {
        if (portfolio.getDateOfPurchase() == null) {
            portfolio.setDateOfPurchase(LocalDate.now());
        }
        return mindlyRepository.save(portfolio);
    }

    /**
     * Deletes the entry in the cryptoportfolio
     *
     * @param portfolioId - portfolio ID
     */
    public void deletePortfolioById(Long portfolioId) {
        mindlyRepository.deleteById(portfolioId);
    }
}
