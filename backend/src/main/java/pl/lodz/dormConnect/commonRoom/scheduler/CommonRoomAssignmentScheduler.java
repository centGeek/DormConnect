package pl.lodz.dormConnect.commonRoom.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomEntity;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomAssignmentEntity;
import pl.lodz.dormConnect.commonRoom.repositories.CommonRoomAssignmentRepository;

import java.util.Calendar;
import java.util.Date;

@Service
public class CommonRoomAssignmentScheduler {

    private final CommonRoomAssignmentRepository assignmentRepository;

    public CommonRoomAssignmentScheduler(CommonRoomAssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }
    private final int TimeAhead = 7;
    //Initialize schedule for common room
    public void createAssignmentsForNextWeek(CommonRoomEntity commonRoom) {
        int maxTimeYouCanStay = commonRoom.getHoursOfTimeWindows();
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        if (calendar.MINUTE != 0){
            calendar.add(Calendar.MINUTE, 60 - calendar.get(Calendar.MINUTE));
        }
        else{calendar.add(Calendar.HOUR,1);}

        for (int i = 0; i < TimeAhead * (24/commonRoom.getHoursOfTimeWindows()); i++) { // Tworzenie przypisań na 7 dni
            Date startDate = calendar.getTime();
            calendar.add(Calendar.HOUR, maxTimeYouCanStay);
            Date endDate = calendar.getTime();

            CommonRoomAssignmentEntity assignment = new CommonRoomAssignmentEntity();
            assignment.setCommonRoom(commonRoom);
            assignment.setStartDate(startDate);
            assignment.setEndDate(endDate);
            assignment.setArchived(false);

            assignmentRepository.save(assignment);
        }
    }

    //archive assigment and add new one
    public void archiveOldAssigment ( CommonRoomAssignmentEntity commonRoomAssignmentEntity) {
        commonRoomAssignmentEntity.setArchived(true);
        assignmentRepository.save(commonRoomAssignmentEntity);

        CommonRoomEntity commonRoom = commonRoomAssignmentEntity.getCommonRoom();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, TimeAhead);

        Date startDate = calendar.getTime();
        calendar.add(Calendar.HOUR, commonRoom.getHoursOfTimeWindows());
        Date endDate = calendar.getTime();

        CommonRoomAssignmentEntity newAssignment = new CommonRoomAssignmentEntity();
        newAssignment.setCommonRoom(commonRoom);
        newAssignment.setStartDate(startDate);
        newAssignment.setEndDate(endDate);
        newAssignment.setArchived(false);
    }

    public void deleteAllActiveAssigmentsForRoom(CommonRoomEntity commonRoom) {
        assignmentRepository.removeCommonRoomAssigmentsByCommonRoomAndArchived(commonRoom, false);
    }

    @Scheduled(fixedRate = 3600000) // co godzinę
    public void scheduleAssignments() {
        // Pobierz wszystkie aktywne pokoje
        for (CommonRoomAssignmentEntity commonRoomAssignmentEntity : assignmentRepository.findAllNotArchivedAssigments()) {
            // Sprawdź, czy są jakieś przypisania do archiwizacji
            if (commonRoomAssignmentEntity.getEndDate().before(new Date())) {
                archiveOldAssigment(commonRoomAssignmentEntity);
            }
        }
    }
}