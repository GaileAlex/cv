package ee.gaile.service.statistics;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Aleksei Gaile 19-Sep-21
 */
public interface UserStatisticsService {

    /**
     * Definition of new or old user
     *
     * @param request - HttpServletRequest
     * @return - sessionId
     */
    Map<String, String> setStatistics(HttpServletRequest request);

}
