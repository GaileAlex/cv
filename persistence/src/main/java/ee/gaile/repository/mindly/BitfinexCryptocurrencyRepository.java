package ee.gaile.repository.mindly;

import ee.gaile.entity.mindly.BitfinexCryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BitfinexCryptocurrencyRepository extends JpaRepository<BitfinexCryptocurrency, Long> {

    Optional<BitfinexCryptocurrency> findByCryptocurrencyName(@Param("cryptocurrencyName") String cryptocurrencyName);



}
