package ee.gaile.repository.mindly;

import ee.gaile.repository.entity.mindly.Portfolio;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PortfolioRepository extends CrudRepository<Portfolio, UUID> {
}
