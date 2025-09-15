package ee.gaile.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.resources.ConnectionProvider;

/**
 * @author Aleksei Gaile 14 Sept 2025
 */
@Configuration
public class ConnectionProviderConfig {

    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider
                .builder("proxy-checker")
                .maxConnections(10_000)
                .pendingAcquireMaxCount(-1)
                .build();
    }

}
