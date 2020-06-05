package ee.gaile.service;

import ee.gaile.entity.mindly.Portfolio;
import ee.gaile.entity.models.Books;
import ee.gaile.service.repository.librarian.BooksRepository;
import ee.gaile.service.repository.mindly.PortfolioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class MindlyService {
    private final BooksRepository booksRepository;
    private final PortfolioRepository portfolioRepository;
    private final BitfinexAccessService bitfinexAccessService;

    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }

    public List<Portfolio> getAllPortfolio() {
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        for (Portfolio portfolio : portfolioList) {
            portfolio.setCurrentMarketValue(portfolio.getAmount()
                    .multiply(bitfinexAccessService.getCurrency(portfolio.getCryptocurrency())));
        }
        return portfolioList;
    }

    public Portfolio savePortfolio(Portfolio portfolio) {
        if (portfolio.getDateOfPurchase() == null) {
            portfolio.setDateOfPurchase(new Date());
        }
        return portfolioRepository.save(portfolio);
    }

    public void deletePortfolioById(Long deleteItem) {
        portfolioRepository.deleteById(deleteItem);
    }
}
