package pl.lodz.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.lodz.security.config.PostgresContainerConfig;

// SpringBootTest loads applicatiion.properties and not from test
@SpringBootTest
@ActiveProfiles("test")
@Import(PostgresContainerConfig.class)
public class AuthUserServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
