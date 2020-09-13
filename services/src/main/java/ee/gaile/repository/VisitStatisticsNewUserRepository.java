package ee.gaile.repository;

import ee.gaile.entity.statistics.VisitStatisticsNewUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitStatisticsNewUserRepository extends JpaRepository<VisitStatisticsNewUser, Long> {

    @Query(value = "select row_number() OVER () AS id, date_trunc('day', first_visit) as visit_date," +
            " count(first_visit) as count_visits from visit_statistics " +
            "WHERE first_visit >= date_trunc('month', current_date - interval '1' month) " +
            "group by  date_trunc('day', first_visit) " +
            "order by visit_date", nativeQuery = true)
    List<VisitStatisticsNewUser> selectNewVisitors();

}
