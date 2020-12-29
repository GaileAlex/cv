package ee.gaile.repository.statistic;

import ee.gaile.models.statistics.VisitStatisticsGraph;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitStatisticUserRepository {
    // language=sql
    private static final String SQL_NEW_USERS = "select date_trunc('day', first_visit) as visit_date, " +
            "             count(first_visit) as count_visits from visit_statistics " +
            "            WHERE first_visit >= ? and  first_visit <= ? " +
            "            group by  date_trunc('day', first_visit) " +
            "            order by visit_date ";
    // language=sql
    private static final String SQL_USERS = "with data as ( " +
            "    select 1 as row, visit_statistics_id, date_trunc('day', visit_date) as visit_date " +
            "    from visit_statistic_user " +
            "WHERE visit_date >= ? and visit_date <= ?) " +
            "select " +
            "    visit_date, " +
            "    sum(row) as count_visits " +
            "from data " +
            "group by visit_date " +
            "order by visit_date ";

    private final JdbcTemplate jdbcTemplate;

    public VisitStatisticUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<VisitStatisticsGraph> selectNewVisitors(LocalDateTime fromDate, LocalDateTime toDate) {

        return jdbcTemplate.query(SQL_NEW_USERS, new BeanPropertyRowMapper<>(VisitStatisticsGraph.class), fromDate, toDate);
    }

    public List<VisitStatisticsGraph> selectVisitStatistic(LocalDateTime fromDate, LocalDateTime toDate) {

        return jdbcTemplate.query(SQL_USERS, new BeanPropertyRowMapper<>(VisitStatisticsGraph.class), fromDate, toDate);
    }

}
