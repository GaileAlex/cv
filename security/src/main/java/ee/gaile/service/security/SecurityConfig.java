package ee.gaile.service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static ee.gaile.service.security.LoginService.*;

@EnableWebSecurity
@Configuration
public class SecurityConfig{

    private final LoginService loginService;

    public SecurityConfig(LoginService loginService) {
        this.loginService = loginService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeRequests()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/proxy/list/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/statistic/user").permitAll()
                .requestMatchers(HttpMethod.GET, "/statistic/graph/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/statistic/file/**").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
                .requestMatchers(HttpMethod.POST, LOGOUT_URL).permitAll()
                .requestMatchers(HttpMethod.POST, AUTH_REFRESH_URL).permitAll()
                .requestMatchers(HttpMethod.GET,  "/blog/comments")
                .authenticated()
                .and()
                .addFilter(new JWTAuthorizationFilter(authenticationManager, loginService));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config.applyPermitDefaultValues());
        return source;
    }

}
