package pl.lodz.security.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.lodz.model.Manager;
import pl.lodz.repository.ManagerRepository;
import pl.lodz.security.AuthUserServiceApplicationTests;
import pl.lodz.security.fixtures.ManagerFixture;
import pl.lodz.service.ManagerRegisterService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@SpringBootTest
@Import(AuthUserServiceApplicationTests.class)
@ActiveProfiles("test")
class ManagerRegisterServiceTest {

    @Autowired
    private ManagerRegisterService managerRegisterService;

    @MockitoBean
    private ManagerRepository managerRepository;

    @Test
    void thatManagerIsRegisteredCorrectly(){
        //given
        Manager manager = ManagerFixture.anyManager();

        //when
        managerRegisterService.registerManager(manager);

        verify(managerRepository).register(any());

    }

}