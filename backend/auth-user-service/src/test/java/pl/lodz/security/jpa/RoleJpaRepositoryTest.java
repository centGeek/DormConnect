package pl.lodz.security.jpa;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import pl.lodz.entity.RoleEntity;
import pl.lodz.repository.jpa.RoleJpaRepository;
import pl.lodz.repository.jpa.UserRepository;
import pl.lodz.security.config.TestApplication;
import pl.lodz.security.fixtures.StudentFixture;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test")
public class RoleJpaRepositoryTest {

    @Autowired
    private RoleJpaRepository roleJpaRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void thatRoleIsRetrievedProperlyByRoleName(){
        //given
        var studentEntity = StudentFixture.anyStudentEntity();
        var userEntity = studentEntity.getUser();
        roleJpaRepository.saveAndFlush(userEntity.getRole());
        userRepository.saveAndFlush(userEntity);

        //when
        RoleEntity studentRole = roleJpaRepository.findByRole("STUDENT");

        //then
        RoleEntity expectedStudentRole = studentEntity.getUser().getRole();
        String actualRoleName = studentRole.getRoleName();
        String expectedRoleName = expectedStudentRole.getRoleName();

        Assertions.assertEquals(expectedRoleName, actualRoleName);
    }
}
