package pl.lodz.commons.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "pl.lodz.commons.repository.jpa")
@EntityScan(basePackages = "pl.lodz.commons.entity")
public class TestApplication {
}
