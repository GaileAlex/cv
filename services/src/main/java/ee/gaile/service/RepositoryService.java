package ee.gaile.service;

import ee.gaile.repository.entity.mindly.Portfolio;
import ee.gaile.repository.entity.models.Books;
import ee.gaile.repository.librarian.BooksRepository;
import ee.gaile.repository.mindly.PortfolioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class RepositoryService {
    private final BooksRepository booksRepository;
    private final PortfolioRepository portfolioRepository;

    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }

    public List<Portfolio> getAllPortfolio() {
        return portfolioRepository.findAll();
    }

    public Portfolio savePortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public void deletePortfolioById(Long deleteItem) {
        portfolioRepository.deleteById(deleteItem);
    }
}
