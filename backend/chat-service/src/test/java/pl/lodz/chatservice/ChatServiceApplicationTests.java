package pl.lodz.chatservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// SpringBootTest loads applicatiion.properties and not from test
@SpringBootTest(properties = "spring.config.location=classpath:/application-test.properties")
class ChatServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
