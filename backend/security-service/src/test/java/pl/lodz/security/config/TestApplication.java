package pl.lodz.security.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"pl.lodz.commons.entity"})
@EnableJpaRepositories(basePackages = "pl.lodz.commons.repository")
public class TestApplication {
}
