package ee.gaile.enums;

import lombok.Getter;

public enum ProxySites {
    PROXY_1("https://www.proxyscan.io/"),
    PROXY_2("https://hidemy.name/en/proxy-list/?type=5#list"),
    PROXY_3("http://proxydb.net/?protocol=socks5&country="),
    PROXY_4("https://www.proxy-list.download/SOCKS5"),
    PROXY_5("https://top-proxies.ru/free_proxy.php"),
    PROXY_6("https://proxylist.live/dashboard/socks5"),
    PROXY_7("https://www.freeproxy.world/?type=socks5");

    @Getter
    private final String url;

    ProxySites(String url) {
        this.url = url;
    }

}
