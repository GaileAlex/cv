package ee.gaile.repository.statistic;

import ee.gaile.models.statistics.VisitStatisticsGraph;
import ee.gaile.models.statistics.VisitStatisticsTable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitStatisticsGraphRepository {
    // language=sql
    private static final String SQL_NEW_USERS = "select date_trunc('day', first_visit) as visit_date, " +
            "             count(first_visit) as count_visits from visit_statistics " +
            "            WHERE first_visit BETWEEN :fromDate and :toDate and user_name != 'Admin' " +
            "            and total_time_on_site > 500 " +
            "            group by  date_trunc('day', first_visit) " +
            "            order by visit_date ";
    // language=sql
    private static final String SQL_USERS = "with data as ( " +
            "    select visit_statistics_id, date_trunc('day', visit_date) as visit_date " +
            "    from visit_statistic_visit_date " +
            "left join visit_statistics vs on vs.id = visit_statistic_visit_date.visit_statistics_id " +
            "WHERE visit_date BETWEEN :fromDate and :toDate and user_name != 'Admin' " +
            "            and total_time_on_site > 500) " +
            "select " +
            "    visit_date, " +
            "    count(visit_date) as count_visits " +
            "from data " +
            "group by visit_date " +
            "order by visit_date ";

    // language=sql
    private static final String SQL_TABLE = "with data as (select distinct on (visit_statistics.id) * " +
            "              from visit_statistics " +
            "                       left join visit_statistics_user_ip vsui on visit_statistics.id = vsui.visit_statistics_id " +
            "              WHERE last_visit BETWEEN :fromDate and :toDate)" +
            "select * from data " +
            "where user_name != 'Admin' " +
            "and total_time_on_site > 500 " +
            "order by last_visit desc limit :pageSize offset :page";

    // language=sql
    private static final String SQL_TABLE_TOTAL = "with data as (select distinct on (user_ip) * " +
            "              from visit_statistics " +
            "                       left join visit_statistics_user_ip vsui on visit_statistics.id = vsui.visit_statistics_id " +
            "              WHERE last_visit BETWEEN :fromDate and :toDate" +
            "              and total_time_on_site > 500) " +
            "select count(user_ip) as total from data " +
            "where user_name != 'Admin' ";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public VisitStatisticsGraphRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<VisitStatisticsGraph> selectNewVisitors(LocalDateTime fromDate, LocalDateTime toDate) {
        MapSqlParameterSource parameters = getDateParameters(fromDate, toDate);
        return jdbcTemplate.query(SQL_NEW_USERS, parameters, new BeanPropertyRowMapper<>(VisitStatisticsGraph.class));
    }

    public List<VisitStatisticsGraph> selectVisitStatistic(LocalDateTime fromDate, LocalDateTime toDate) {
        MapSqlParameterSource parameters = getDateParameters(fromDate, toDate);
        return jdbcTemplate.query(SQL_USERS, parameters, new BeanPropertyRowMapper<>(VisitStatisticsGraph.class));
    }

    public List<VisitStatisticsTable> findByDate(LocalDateTime fromDate, LocalDateTime toDate,
                                                 Integer pageSize, Integer page) {
        MapSqlParameterSource parameters = getDateParameters(fromDate, toDate);
        parameters.addValue("pageSize", pageSize);
        parameters.addValue("page", page);
        return jdbcTemplate.query(SQL_TABLE, parameters, new BeanPropertyRowMapper<>(VisitStatisticsTable.class));
    }

    public Long countTotal(LocalDateTime fromDate, LocalDateTime toDate) {
        MapSqlParameterSource parameters = getDateParameters(fromDate, toDate);
        return jdbcTemplate.queryForObject(SQL_TABLE_TOTAL, parameters, Long.class);
    }

    private MapSqlParameterSource getDateParameters(LocalDateTime fromDate, LocalDateTime toDate) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("fromDate", fromDate);
        parameters.addValue("toDate", toDate);
        return parameters;
    }

}
