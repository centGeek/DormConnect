package pl.lodz.eventservice;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.config.location=classpath:/application-test.properties")
public class EventServiceApplicationTests {

    @Test
    public void contextLoads() {
    }

}
