package pl.lodz.dormConnect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class DormConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DormConnectApplication.class, args);
    }

}
