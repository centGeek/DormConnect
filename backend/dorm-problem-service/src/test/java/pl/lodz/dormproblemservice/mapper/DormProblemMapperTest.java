package pl.lodz.dormproblemservice.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.dormproblemservice.dto.GetDormProblemDTO;
import pl.lodz.dormproblemservice.entity.DormProblemEntity;
import pl.lodz.dormproblemservice.fixtures.DormProblemFixtures;
class DormProblemMapperTest {

    @Test
    void thatMappingToEntityWorksCorrectly(){
        //given
        DormProblemEntity dormProblemEntity = DormProblemFixtures.anyDormProblemEntity();

        //when
        GetDormProblemDTO getDormProblemDTO = DormProblemMapper.mapToGetDTO(dormProblemEntity, "You are my friend");

        //then
        Assertions.assertEquals(dormProblemEntity.getProblemDate(), getDormProblemDTO.problemDate());
        Assertions.assertEquals(dormProblemEntity.getProblemStatus(), getDormProblemDTO.problemStatus());
        Assertions.assertEquals(dormProblemEntity.getAnswer(), getDormProblemDTO.answer());
        Assertions.assertEquals(dormProblemEntity.getDescription(), getDormProblemDTO.description());
    }

    @Test
    void thatMappingToDTOWorksCorrectly(){
        //given
        GetDormProblemDTO getDormProblemDTO = DormProblemFixtures.anyDormProblemDTO();

        //when
        DormProblemEntity dormProblemEntity = DormProblemMapper.mapToGetEntity(getDormProblemDTO);

        //then
        Assertions.assertEquals(dormProblemEntity.getProblemDate(), getDormProblemDTO.problemDate());
        Assertions.assertEquals(dormProblemEntity.getProblemStatus(), getDormProblemDTO.problemStatus());
        Assertions.assertEquals(dormProblemEntity.getAnswer(), getDormProblemDTO.answer());
        Assertions.assertEquals(dormProblemEntity.getDescription(), getDormProblemDTO.description());

    }

}