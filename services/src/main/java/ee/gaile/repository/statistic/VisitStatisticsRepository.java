package ee.gaile.repository.statistic;

import ee.gaile.entity.statistics.VisitStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitStatisticsRepository extends JpaRepository<VisitStatistics, Long> {
    @Query(value = "select * from visit_statistics " +
            "left join visit_statistics_user_ip vsui " +
            "    on visit_statistics.id = vsui.visit_statistics_id " +
            "where user_ip = :userIP limit 1", nativeQuery = true)
    Optional<VisitStatistics> findByUserIP(@Param("userIP") String userIP);

    @Query(value = "select * from visit_statistics " +
            "            where user_name = :userName " +
            "order by last_visit desc limit 1", nativeQuery = true)
    Optional<VisitStatistics> findByUserName(@Param("userName") String userName);

    @Query(value = "select * from visit_statistics " +
            "left join visit_statistics_user_ip vsui " +
            "    on visit_statistics.id = vsui.visit_statistics_id " +
            "where session_id = :sessionId limit 1", nativeQuery = true)
    Optional<VisitStatistics> findBySessionId(@Param("sessionId") String sessionId);

}
