package ee.gaile.repository;

import ee.gaile.entity.statistics.VisitStatisticsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitStatisticsUserRepository extends JpaRepository<VisitStatisticsUser, Long> {

    @Query(value = "with data as ( " +
            "    select id, visit_statistics_id as count_visit, date_trunc('day', visit_date) as visit_date " +
            "    from visit_statistic_user " +
            ") " +
            "select  row_number() OVER () AS id, " +
            "    visit_date, " +
            "    sum(count_visit) as count_visits " +
            "from data " +
            "group by visit_date " +
            "order by visit_date ",
            nativeQuery = true)
    List<VisitStatisticsUser> selectVisitStatistic();
}
