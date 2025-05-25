package pl.lodz.dormConnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
    "pl.lodz.dormConnect",
    "pl.lodz.commons"
})
@EnableScheduling
@EntityScan(basePackages = {"pl.lodz"})
@EnableJpaRepositories(basePackages = "pl.lodz")
@EnableDiscoveryClient
public class DormConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DormConnectApplication.class, args);
    }

}
