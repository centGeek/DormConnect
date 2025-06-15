package pl.lodz.dormproblemservice;

import config.PostgresContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(PostgresContainerConfig.class)
class DormProblemServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
