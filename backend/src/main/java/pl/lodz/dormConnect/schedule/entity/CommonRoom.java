package pl.lodz.dormConnect.schedule.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class CommonRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private int capacity;
    private int floor;
    private int maxTimeYouCanStay;
    private boolean active = true;

    @OneToMany
    private List<CommonRoomAssigment> commonRoomAssign = new ArrayList<>();
}
