package pl.lodz.dormConnect.database.repository.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import pl.lodz.dormConnect.config.PersistenceContainersTestConfiguration;
import pl.lodz.dormConnect.fixtures.SecurityFixture;
import pl.lodz.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainersTestConfiguration.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Test
    void thatUserIsCorrectlyRetrievedByEmail(){
        //when
        var userEntity = userRepository.findByEmail("admin@edu.p.lodz.pl").get();
        //then
        var expectedUserEntity = SecurityFixture.adminEntity();
        Assertions.assertEquals(expectedUserEntity, userEntity);
    }

}