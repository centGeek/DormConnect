package pl.lodz.dormitoryservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.lodz.dormitoryservice.config.PostgresContainerConfig;

@SpringBootTest
@ActiveProfiles("test")
@Import(PostgresContainerConfig.class)
class DormitoryServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
