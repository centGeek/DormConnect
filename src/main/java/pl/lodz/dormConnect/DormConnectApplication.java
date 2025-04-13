package pl.lodz.dormConnect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.lodz.dormConnect.security.repository.UserRepository;

@SpringBootApplication
@Slf4j
public class DormConnectApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DormConnectApplication.class, args);
        UserRepository bean = run.getBean(UserRepository.class);

        //log that users are created
        log.info("Created users: " + bean.findAll());
    }

}
