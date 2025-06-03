package pl.lodz.security.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.lodz.commons.model.Manager;
import pl.lodz.security.config.TestApplication;
import pl.lodz.security.fixtures.ManagerFixture;
import pl.lodz.commons.repository.ManagerRepository;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@SpringBootTest
@Import(TestApplication.class)
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