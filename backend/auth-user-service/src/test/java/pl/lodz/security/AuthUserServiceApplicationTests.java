package pl.lodz.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// SpringBootTest loads applicatiion.properties and not from test
@SpringBootTest(properties = "spring.config.location=classpath:/application-test.properties")
public class AuthUserServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
