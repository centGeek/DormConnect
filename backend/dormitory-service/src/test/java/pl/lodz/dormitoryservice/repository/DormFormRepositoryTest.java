package pl.lodz.dormitoryservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormitoryservice.entity.DormFormEntity;
import pl.lodz.dormitoryservice.fixtures.DormFormFixtures;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DormFormRepositoryTest {

    @Autowired
    private DormFormRepository dormFormRepository;

    @Test
    public void thatUserRetrievingWorksCorrectly() {
        //given
        DormFormEntity dormFormEntity = DormFormFixtures.anyDormFormEntity();

        //when
        dormFormRepository.save(dormFormEntity);

        //then
        List<DormFormEntity> byUserId = dormFormRepository.findByUserId(dormFormEntity.getUserId());
        int actualSize = byUserId.size();
        Assertions.assertEquals(1, actualSize);
    }

    @Test
    public void thatConflictingUnprocessedFormWasFound(){
        DormFormEntity dormFormEntity = DormFormFixtures.conflictingDormFormEntity();

        //when
        dormFormRepository.save(dormFormEntity);

        //then
        List<DormFormEntity> byUserId = dormFormRepository.findConflictingUnprocessedForms(
                dormFormEntity.getUserId(), dormFormEntity.getStartDate(), dormFormEntity.getEndDate()
        );

        int actualSize = byUserId.size();
        Assertions.assertEquals(1, actualSize);

    }

}