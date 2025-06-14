package pl.lodz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.lodz.repository.jpa.UserRepository;

@Component
public class StartupRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(StartupRunner.class);

    public StartupRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        // log that users are created
        log.info("Created users: " + userRepository.findAll());
    }
}
