package ee.gaile;

import ee.gaile.sync.mindly.BitfinexAccessService;
import ee.gaile.sync.proxy.ProxyCheck;
import ee.gaile.sync.proxy.ProxyListService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {CVApplication.class})
@ActiveProfiles("test")
@Rollback(false)
@Transactional
@SpringBootConfiguration
public abstract class ApplicationIT {

}
