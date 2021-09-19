package ee.gaile.repository.mindly;

import ee.gaile.entity.mindly.BitfinexCryptocurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BitfinexCryptocurrencyRepository extends JpaRepository<BitfinexCryptocurrencyEntity, Long> {

    Optional<BitfinexCryptocurrencyEntity> findByCryptocurrencyName(@Param("cryptocurrencyName") String cryptocurrencyName);

}
