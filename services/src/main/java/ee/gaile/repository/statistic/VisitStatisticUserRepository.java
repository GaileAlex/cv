package ee.gaile.repository.statistic;

import ee.gaile.entity.statistics.VisitStatisticsGraph;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitStatisticUserRepository {
    // language=sql
    private static final String SQL_NEW_USERS = "select date_trunc('day', first_visit) as visit_date, " +
            "             count(first_visit) as count_visits from visit_statistics " +
            "            WHERE first_visit >= date_trunc('month', current_date - interval '1' month) " +
            "            group by  date_trunc('day', first_visit) " +
            "            order by visit_date ";
    // language=sql
    private static final String SQL_USERS = "with data as ( " +
            "    select 1 as row, visit_statistics_id, date_trunc('day', visit_date) as visit_date " +
            "    from visit_statistic_user " +
            "WHERE visit_date >= date_trunc('month', current_date - interval '1' month)) " +
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

    public List<VisitStatisticsGraph> selectNewVisitors() {

        return jdbcTemplate.query(SQL_NEW_USERS, new BeanPropertyRowMapper<>(VisitStatisticsGraph.class));
    }

    public List<VisitStatisticsGraph> selectVisitStatistic() {

        return jdbcTemplate.query(SQL_USERS, new BeanPropertyRowMapper<>(VisitStatisticsGraph.class));
    }

}
