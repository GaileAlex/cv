package ee.gaile.CV.mindly.postgresql;

import ee.gaile.CV.mindly.models.Portfolio;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PortfolioRepository extends CrudRepository<Portfolio, UUID> {
}
