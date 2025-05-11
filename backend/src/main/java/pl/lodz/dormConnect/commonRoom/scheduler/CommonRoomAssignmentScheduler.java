package pl.lodz.dormConnect.commonRoom.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomEntity;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomAssignmentEntity;
import pl.lodz.dormConnect.commonRoom.repositories.CommonRoomAssignmentRepository;
import pl.lodz.dormConnect.commonRoom.repositories.CommonRoomRepository;

import java.util.Calendar;
import java.util.Date;

@Service
public class CommonRoomAssignmentScheduler {

    private final CommonRoomAssignmentRepository assignmentRepository;
    private final CommonRoomRepository commonRoomRepository;

    public CommonRoomAssignmentScheduler(CommonRoomAssignmentRepository assignmentRepository, CommonRoomRepository commonRoomRepository) {
        this.assignmentRepository = assignmentRepository;
        this.commonRoomRepository = commonRoomRepository;
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
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.HOUR, 24-calendar.get(Calendar.HOUR_OF_DAY));

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


    public void deleteAllAssigmentsForRoom(CommonRoomEntity commonRoom) {
        assignmentRepository.removeCommonRoomAssigmentsByCommonRoom(commonRoom);
    }

    @Scheduled(fixedRate = 3600000) // co godzinę
    public void scheduleAssignments() {
        // Pobierz wszystkie aktywne pokoje
        for (CommonRoomAssignmentEntity commonRoomAssignmentEntity : assignmentRepository.findAllNotArchivedAssigments()) {
            // Sprawdź, czy są jakieś przypisania do archiwizacji
            if (commonRoomAssignmentEntity.getEndDate().before(new Date())) {
                commonRoomAssignmentEntity.setArchived(true);
            }
        }
    }
    @Scheduled(cron="0 5 0  * * ?") // everyday at 00:05 function that creates new assigments for every room
    public void updateSchedule(){
        assignmentRepository.removeAllByArchived(true); //delete all old assignments
        for (CommonRoomEntity commonRoom: commonRoomRepository.getAllCommonRooms()){

            int maxTimeYouCanStay = commonRoom.getHoursOfTimeWindows();
            Date currentDate = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR, TimeAhead-1);
            if (calendar.MINUTE != 0){
                calendar.add(Calendar.MINUTE, 60 - calendar.get(Calendar.MINUTE));
            }
            else{calendar.add(Calendar.HOUR,1);}
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.HOUR, 24-calendar.get(Calendar.HOUR_OF_DAY));

            for (int i = 0; i < (24/commonRoom.getHoursOfTimeWindows()); i++) { // Tworzenie przypisań na 7 dni
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
    }
}