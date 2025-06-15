package pl.lodz.chatservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@TestConfiguration
public class PostgresContainerConfig {

    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:17"))
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test")
                    .withExposedPorts(5432);

    static {
        POSTGRES_CONTAINER.setPortBindings(
                java.util.Collections.singletonList("5436:5432")
        );
        POSTGRES_CONTAINER.start();
    }

    @Bean
    public PostgreSQLContainer<?> postgresContainer() {
        return POSTGRES_CONTAINER;
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () ->
                "jdbc:postgresql://localhost:5436/" + POSTGRES_CONTAINER.getDatabaseName());
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRES_CONTAINER::getDriverClassName);

        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }
}
