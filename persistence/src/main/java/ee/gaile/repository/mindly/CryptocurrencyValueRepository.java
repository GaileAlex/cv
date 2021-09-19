package ee.gaile.repository.mindly;

import ee.gaile.entity.mindly.CryptocurrencyValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptocurrencyValueRepository extends JpaRepository<CryptocurrencyValueEntity, Long> {

    @Query(value = "select  cv.id, date_cryptocurrency, value_cryptocurrency, bitfinex_cryptocurrency_id " +
            "from bitfinex_cryptocurrency " +
            "left join cryptocurrency_values cv on bitfinex_cryptocurrency.id = cv.bitfinex_cryptocurrency_id " +
            "where cryptocurrency_name = :cryptocurrencyName " +
            "order by date_cryptocurrency desc limit 1 ", nativeQuery = true)
    CryptocurrencyValueEntity findByCryptocurrencyNameAndLastDate(@Param("cryptocurrencyName") String cryptocurrencyName);
}
