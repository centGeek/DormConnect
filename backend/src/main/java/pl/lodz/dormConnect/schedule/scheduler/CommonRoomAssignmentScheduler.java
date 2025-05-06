package pl.lodz.dormConnect.schedule.scheduler;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.schedule.entity.CommonRoom;
import pl.lodz.dormConnect.schedule.entity.CommonRoomAssigment;
import pl.lodz.dormConnect.schedule.repositories.CommonRoomAssigmentRepository;

import java.util.Calendar;
import java.util.Date;

@Service
public class CommonRoomAssignmentScheduler {

    private final CommonRoomAssigmentRepository assignmentRepository;

    public CommonRoomAssignmentScheduler(CommonRoomAssigmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }
    private final int TimeAhead = 7;
    //Initialize schedule for common room
    public void createAssignmentsForNextWeek(CommonRoom commonRoom) {
        int maxTimeYouCanStay = commonRoom.getMaxTimeYouCanStay();
        Date currentDate = new Date();

        for (int i = 0; i < TimeAhead; i++) { // Tworzenie przypisań na 7 dni
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR, i);

            Date startDate = calendar.getTime();
            calendar.add(Calendar.HOUR, maxTimeYouCanStay);
            Date endDate = calendar.getTime();

            CommonRoomAssigment assignment = new CommonRoomAssigment();
            assignment.setCommonRoom(commonRoom);
            assignment.setStartDate(startDate);
            assignment.setEndDate(endDate);
            assignment.setArchived(false);

            assignmentRepository.save(assignment);
        }
    }

    //archive assigment and add new one
    public void archiveOldAssigment ( CommonRoomAssigment commonRoomAssigment) {
        commonRoomAssigment.setArchived(true);
        assignmentRepository.save(commonRoomAssigment);

        CommonRoom commonRoom = commonRoomAssigment.getCommonRoom();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, TimeAhead);

        Date startDate = calendar.getTime();
        calendar.add(Calendar.HOUR, commonRoom.getMaxTimeYouCanStay());
        Date endDate = calendar.getTime();

        CommonRoomAssigment newAssignment = new CommonRoomAssigment();
        newAssignment.setCommonRoom(commonRoom);
        newAssignment.setStartDate(startDate);
        newAssignment.setEndDate(endDate);
        newAssignment.setArchived(false);
    }

    //@Scheduled(fixedRate = 3600000) // co godzinę
    public void scheduleAssignments() {
        // Pobierz wszystkie aktywne pokoje
        for (CommonRoomAssigment commonRoomAssigment : assignmentRepository.findAllNotArchivedAssigments()) {
            // Sprawdź, czy są jakieś przypisania do archiwizacji
            if (commonRoomAssigment.getEndDate().before(new Date())) {
                archiveOldAssigment(commonRoomAssigment);
            }
        }
    }
}