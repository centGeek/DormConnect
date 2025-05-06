package pl.lodz.dormConnect.schedule.entity;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "common_room")
public class CommonRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "common_room_id")
    private Long id;

    @Column (name = "common_room_type")
    private String type;

    @Column (name = "capacity")
    private int capacity;

    @Column (name = "floor")
    private int floor;

    @Column (name = "max_time_you_can_stay")
    private int maxTimeYouCanStay;

    @Column (name = "is_active")
    private boolean active = true;

    @OneToMany(mappedBy = "commonRoom")
    private List<CommonRoomAssigment> commonRoomAssign = new ArrayList<>();
}
