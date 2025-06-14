package pl.lodz.dormitoryservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Setter
@Getter
@Table (name = "common_room")
@AllArgsConstructor
@NoArgsConstructor
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

    public CommonRoomEntity(CommonRoomType commonRoomType, int capacity, int floor, int hoursOfTimeWindows, int howManyTimesAWeekYouCanUseIt, boolean active, List<CommonRoomAssignmentEntity> commonRoomAssign) {
        this.commonRoomType = commonRoomType;
        this.capacity = capacity;
        this.floor = floor;
        this.hoursOfTimeWindows = hoursOfTimeWindows;
        this.howManyTimesAWeekYouCanUseIt = howManyTimesAWeekYouCanUseIt;
        this.active = active;
        this.commonRoomAssign = commonRoomAssign;
    }

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
