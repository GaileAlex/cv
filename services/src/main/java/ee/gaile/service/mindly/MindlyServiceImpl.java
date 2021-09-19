package ee.gaile.service.mindly;

import ee.gaile.entity.mindly.MindlyEntity;
import ee.gaile.models.mindly.Mindly;
import ee.gaile.repository.mindly.CryptocurrencyValueRepository;
import ee.gaile.repository.mindly.MindlyRepository;
import ee.gaile.service.utils.MapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class MindlyServiceImpl implements MindlyService {
    private final MindlyRepository mindlyRepository;
    private final CryptocurrencyValueRepository cryptocurrencyValueRepository;
    private final MapperUtils mapperUtils;

    @Override
    public List<Mindly> getAllPortfolio() {
        List<MindlyEntity> portfolioList = mindlyRepository.findAll();
        List<Mindly> mindlies = mapperUtils.toOtherList(portfolioList, Mindly.class);

        mindlies.forEach(portfolio -> {
            BigDecimal value = cryptocurrencyValueRepository
                    .findByCryptocurrencyNameAndLastDate(portfolio.getCryptocurrency()).getValueCurrency()
                    .setScale(2, RoundingMode.HALF_EVEN);

            portfolio.setCurrentMarketValue(portfolio.getAmount()
                    .multiply(value));
        });

        return mindlies;
    }

    @Override
    public Mindly savePortfolio(@RequestBody Mindly portfolio) {
        if (portfolio.getDateOfPurchase() == null) {
            portfolio.setDateOfPurchase(LocalDate.now());
        }
        mindlyRepository.save(mapperUtils.toOtherClass(portfolio, MindlyEntity.class));

        return portfolio;
    }

    @Override
    public void deletePortfolioById(Long portfolioId) {
        mindlyRepository.deleteById(portfolioId);
    }
}
