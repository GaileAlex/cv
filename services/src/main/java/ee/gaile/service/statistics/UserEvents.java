package ee.gaile.service.statistics;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Aleksei Gaile 19-Sep-21
 */
public interface UserEvents {

    /**
     * Saves user actions on the site in the database
     *
     * @param request - HttpServletRequest
     */
    void setUserEvent(HttpServletRequest request);

    /**
     * Saves the time the user spends on the site
     *
     * @param request - HttpServletRequest
     */
    void setUserTotalTimeOnSite(HttpServletRequest request);
}
