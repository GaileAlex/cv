package ee.gaile.service.proxy;

import ee.gaile.models.proxy.ProxyListWrapper;

/**
 * @author Aleksei Gaile 19-Sep-21
 */
public interface ProxyService {

    /**
     * Getting data for proxy table
     *
     * @param pageSize - number of pages in the table
     * @param page     - page number
     * @return - ProxyListWrapper proxy list and number of pages
     */
    ProxyListWrapper getProxy(Integer pageSize, Integer page);

}
