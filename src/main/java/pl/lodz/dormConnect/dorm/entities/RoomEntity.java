package pl.lodz.dormConnect.dorm.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "DormRooms")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;
    private int capacity;
    private int floor;

}
