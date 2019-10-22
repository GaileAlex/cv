package ee.gaile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

@PropertySource(value = "file:config/cv-api.properties", ignoreResourceNotFound = true)
@SpringBootApplication
public class CVApplication extends SpringBootServletInitializer {

    /**
     * Program entry point.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CVApplication.class, args);

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CVApplication.class);
    }

    @Bean
    ErrorViewResolver supportPathBasedLocationStrategyWithoutHashes() {
        return (request, status, model) -> status == HttpStatus.NOT_FOUND
                ? new ModelAndView("404.html", Collections.emptyMap(), HttpStatus.OK)
                : null;
    }
}
