package pl.lodz.dormitoryservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormitoryservice.config.PostgresContainerConfig;
import pl.lodz.dormitoryservice.entity.RoomAssignEntity;
import pl.lodz.dormitoryservice.fixtures.RoomsAssignFixtures;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(PostgresContainerConfig.class)
class RoomAssignmentRepositoryTest {

    @Autowired
    private RoomAssignmentRepository roomAssignmentRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void thatFindAssignmentsForRoomInPeriodWorksCorrectly() {
        //given
        RoomAssignEntity roomAssignEntity = RoomsAssignFixtures.anyRoomAssignEntity();
        roomRepository.save(roomAssignEntity.getRoom());

        //when
        roomAssignmentRepository.save(roomAssignEntity);

        List<RoomAssignEntity> assignmentsForRoomInPeriod = roomAssignmentRepository.findAssignmentsForRoomInPeriod(
                roomAssignEntity.getId(), roomAssignEntity.getFromDate(), roomAssignEntity.getToDate());

        //then
        int expectedSize = 1;
        Assertions.assertEquals(expectedSize, assignmentsForRoomInPeriod.size());
    }

    @Test
    public void thatFindAllAssignmentsByStudentIdWorksCorrectly() {
        //given
        RoomAssignEntity roomAssignEntity = RoomsAssignFixtures.anotherRoomAssignEntity();
        roomRepository.save(roomAssignEntity.getRoom());

        //when
        roomAssignmentRepository.save(roomAssignEntity);

        List<RoomAssignEntity> assignmentsForRoomInPeriod = roomAssignmentRepository.findAllAssignmentsByStudentId(
                roomAssignEntity.getResidentId());

        boolean actualResult = roomAssignmentRepository.existsAssignmentAtDate(roomAssignEntity.getResidentId(),
                roomAssignEntity.getFromDate().plusDays(1), roomAssignEntity.getRoom().getNumber());

        //then
        int expectedSize = 1;
        Assertions.assertEquals(expectedSize, assignmentsForRoomInPeriod.size());
        Assertions.assertTrue(actualResult);
    }


}