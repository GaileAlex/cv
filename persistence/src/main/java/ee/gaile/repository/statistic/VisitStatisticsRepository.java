package ee.gaile.repository.statistic;

import ee.gaile.entity.statistics.VisitStatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitStatisticsRepository extends JpaRepository<VisitStatisticsEntity, Long> {
    @Query(value = "select * from visit_statistics " +
            "left join visit_statistics_user_ip vsui " +
            "    on visit_statistics.id = vsui.visit_statistics_id " +
            "where user_ip = :userIP limit 1", nativeQuery = true)
    Optional<VisitStatisticsEntity> findByUserIP(@Param("userIP") String userIP);

    Optional<VisitStatisticsEntity> findBySessionId(@Param("sessionId") String sessionId);

}
