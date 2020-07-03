package ee.gaile.service.security.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@EnableConfigurationProperties(SuperAdminConfig.class)
@ConfigurationProperties(prefix="cv.super-admin")
public class SuperAdminConfig {

    private List<String> users = new ArrayList<>();

    public List<String> getUsers() {
        return this.users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
