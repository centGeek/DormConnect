package pl.lodz.dormitoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class DormitoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DormitoryServiceApplication.class, args);
    }

}
