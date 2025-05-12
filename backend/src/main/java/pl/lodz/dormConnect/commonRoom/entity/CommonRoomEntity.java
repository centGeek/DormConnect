package pl.lodz.dormConnect.commonRoom.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Setter
@Getter
@Table (name = "common_room")
public class CommonRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "common_room_id")
    private Long id;

    @Column (name = "common_room_type")
    private CommonRoomType commonRoomType;

    @Column (name = "capacity")
    private int capacity;

    @Column (name = "floor")
    private int floor;

    @Column (name = "hours_of_time_windows")
    private int hoursOfTimeWindows;

    @Column (name = "how_many_times_a_week_you_can_use_it")
    private int howManyTimesAWeekYouCanUseIt;

    @Column (name = "is_active")
    private boolean active = true;

    @OneToMany(mappedBy = "commonRoom")
    @JsonIgnore
    private List<CommonRoomAssignmentEntity> commonRoomAssign = new ArrayList<>();

    public enum CommonRoomType {
        STUDY_ROOM,
        GYM,
        LAUNDRY,
        BILLARD_ROOM,
        TV_ROOM,
        FITNESS_ROOM,
        TABLE_TENNIS_ROOM
    }
}
