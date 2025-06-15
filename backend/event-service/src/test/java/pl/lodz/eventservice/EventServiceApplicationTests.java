package pl.lodz.eventservice;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.lodz.eventservice.config.PostgresContainerConfig;

@SpringBootTest
@ActiveProfiles("test")
@Import(PostgresContainerConfig.class)
public class EventServiceApplicationTests {

    @Test
    public void contextLoads() {
    }

}
