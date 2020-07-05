package ee.gaile.service.mindly;

import ee.gaile.entity.mindly.Mindly;
import ee.gaile.entity.librarian.Books;
import ee.gaile.service.repository.librarian.LibrarianRepository;
import ee.gaile.service.repository.mindly.MindlyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class MindlyService {
    private final LibrarianRepository booksRepository;
    private final MindlyRepository mindlyRepository;
    private final BitfinexAccessService bitfinexAccessService;

    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }

    public List<Mindly> getAllPortfolio() {
        List<Mindly> portfolioList = mindlyRepository.findAll();

        portfolioList.forEach((portfolio)-> portfolio.setCurrentMarketValue(portfolio.getAmount()
                .multiply(bitfinexAccessService.getCurrency(portfolio.getCryptocurrency()))));

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
