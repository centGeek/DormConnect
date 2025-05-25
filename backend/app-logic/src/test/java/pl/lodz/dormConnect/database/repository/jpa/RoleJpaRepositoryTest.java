package pl.lodz.dormConnect.database.repository.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import pl.lodz.dormConnect.config.PersistenceContainersTestConfiguration;
import pl.lodz.dormConnect.fixtures.SecurityFixture;
import pl.lodz.entity.RoleEntity;
import pl.lodz.repository.jpa.RoleJpaRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainersTestConfiguration.class)
public class RoleJpaRepositoryTest {

    @Autowired
    private RoleJpaRepository roleJpaRepository;

    @Test
    public void thatRoleIsRetrievedProperlyByRoleName(){
        //when
        RoleEntity studentRole = roleJpaRepository.findByRole("ADMIN");

        //then
        RoleEntity expectedStudentRole = SecurityFixture.adminRole();
        Assertions.assertEquals(expectedStudentRole, studentRole);
    }
}
