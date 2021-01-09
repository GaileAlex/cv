package ee.gaile.repository.proxy;

import ee.gaile.entity.proxy.ProxyList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProxyRepository extends JpaRepository<ProxyList, Long> {

    @Query(value = "select * from proxy_list " +
            " where speed > 0 " +
            " order by speed desc limit :pageSize offset :page ", nativeQuery = true)
    List<ProxyList> findWithPaging(@Param("pageSize") Integer pageSize, @Param("page") Integer page);

    @Query(value = "select count(id) from proxy_list " +
            " where speed > 0 ", nativeQuery = true)
    Long getTotal();

    @Query(value = "select * from proxy_list " +
            " where country = 'unknown' and uptime > 0", nativeQuery = true)
    List<ProxyList> findAllWhereCountryUnknown();

    @Query(value = "select * from proxy_list " +
            " order by random()", nativeQuery = true)
    List<ProxyList> findAllOrderByRandom();

}
