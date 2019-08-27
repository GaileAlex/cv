package ee.gaile.CV.mindly.postgresql;

import ee.gaile.CV.mindly.models.Portfolio;
import org.springframework.data.repository.CrudRepository;

public interface PortfolioRepository extends CrudRepository<Portfolio, Long> {
}
