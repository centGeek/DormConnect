package pl.lodz.dormConnect.security.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.lodz.dormConnect.config.PersistenceContainersTestConfiguration;
import pl.lodz.dormConnect.domain.Manager;
import pl.lodz.dormConnect.fixtures.ManagerFixture;

import java.util.List;

@SpringBootTest
@Import(PersistenceContainersTestConfiguration.class)
class ManagerRegisterServiceTest {

    @Autowired
    private ManagerRegisterService managerRegisterService;

    @Test
    void thatStudentIsRegisteredCorrectly(){
        //given
        Manager manager = ManagerFixture.anyManager();

        //when
        managerRegisterService.registerStudent(manager);

        List<Manager> allManagers = managerRegisterService.getAllStudents();

        //then
        Assertions.assertThat(allManagers)
                .extracting(Manager::surname)
                .contains(manager.surname());
    }

}