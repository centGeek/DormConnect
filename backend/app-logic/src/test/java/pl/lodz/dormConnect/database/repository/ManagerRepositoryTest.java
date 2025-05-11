package pl.lodz.dormConnect.database.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.lodz.dormConnect.DormConnectApplication;
import pl.lodz.dormConnect.config.PersistenceContainersTestConfiguration;
import pl.lodz.dormConnect.fixtures.ManagerFixture;

@SpringBootTest(classes = DormConnectApplication.class)
@Import(PersistenceContainersTestConfiguration.class)
class ManagerRepositoryTest {

    @Autowired
    private ManagerRepository managerRepository;

    @Test
    void thatManagerIsRegisteredAndRetrievedCorrectly() {
        var manager = ManagerFixture.anyManager();

        //when
        managerRepository.register(manager);

        //then
        var actualManager = managerRepository.getByEmail(manager.user().getEmail()).get();
        Assertions.assertEquals(manager, actualManager);
    }

}