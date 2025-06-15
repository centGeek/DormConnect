package pl.lodz.chatservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.lodz.chatservice.config.PostgresContainerConfig;

@SpringBootTest
@ActiveProfiles("test")
@Import(PostgresContainerConfig.class)
class ChatServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
