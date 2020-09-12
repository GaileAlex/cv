package ee.gaile.service.mindly;

import ee.gaile.entity.mindly.Mindly;
import ee.gaile.repository.mindly.CryptocurrencyValueRepository;
import ee.gaile.repository.mindly.MindlyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class MindlyService {
    private final MindlyRepository mindlyRepository;
    private final CryptocurrencyValueRepository cryptocurrencyValueRepository;

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

    public Mindly savePortfolio(Mindly portfolio) {
        if (portfolio.getDateOfPurchase() == null) {
            portfolio.setDateOfPurchase(new Date());
        }
        return mindlyRepository.save(portfolio);
    }

    public void deletePortfolioById(Long portfolioId) {
        mindlyRepository.deleteById(portfolioId);
    }
}
