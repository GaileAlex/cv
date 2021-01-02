package ee.gaile.repository.statistic;

import ee.gaile.models.statistics.VisitStatisticsGraph;
import ee.gaile.models.statistics.VisitStatisticsTable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitStatisticsGraphRepository {
    // language=sql
    private static final String SQL_NEW_USERS = "select date_trunc('day', last_visit) as visit_date, " +
            "             count(last_visit) as count_visits from visit_statistics " +
            "            WHERE last_visit BETWEEN ? and ? and user_name != 'Admin' " +
            "            group by  date_trunc('day', last_visit) " +
            "            order by visit_date ";
    // language=sql
    private static final String SQL_USERS = "with data as ( " +
            "    select 1 as row, visit_statistics_id, date_trunc('day', visit_date) as visit_date " +
            "    from visit_statistic_visit_date " +
            "left join visit_statistics vs on vs.id = visit_statistic_visit_date.visit_statistics_id " +
            "WHERE visit_date BETWEEN ? and ? and user_name != 'Admin' ) " +
            "select " +
            "    visit_date, " +
            "    sum(row) as count_visits " +
            "from data " +
            "group by visit_date " +
            "order by visit_date ";

    // language=sql
    private static final String SQL_TABLE = "with data as (select distinct on (user_ip) * " +
            "              from visit_statistics " +
            "                       left join visit_statistics_user_ip vsui on visit_statistics.id = vsui.visit_statistics_id " +
            "              WHERE last_visit BETWEEN ? and ?)" +
            "select * from data " +
            "where user_name != 'Admin' " +
            "order by last_visit desc";

    private final JdbcTemplate jdbcTemplate;

    public VisitStatisticsGraphRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<VisitStatisticsGraph> selectNewVisitors(LocalDateTime fromDate, LocalDateTime toDate) {
        return jdbcTemplate.query(SQL_NEW_USERS, new BeanPropertyRowMapper<>(VisitStatisticsGraph.class), fromDate, toDate);
    }

    public List<VisitStatisticsGraph> selectVisitStatistic(LocalDateTime fromDate, LocalDateTime toDate) {
        return jdbcTemplate.query(SQL_USERS, new BeanPropertyRowMapper<>(VisitStatisticsGraph.class), fromDate, toDate);
    }

    public List<VisitStatisticsTable> findByDate(LocalDateTime fromDate, LocalDateTime toDate) {
        return jdbcTemplate.query(SQL_TABLE, new BeanPropertyRowMapper<>(VisitStatisticsTable.class), fromDate, toDate);
    }

}
