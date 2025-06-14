package pl.lodz.dormitoryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "floor")
public class FloorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "floor_id")
    private Long id;

    @Column(name = "floor_number")
    private int floorNumber;

    private List<Long> rooms = new ArrayList<>();

    private List<Long> commonRooms = new ArrayList<>();

    public FloorEntity(int floorNumber, List<Long> rooms, List<Long> commonRooms) {
        this.floorNumber = floorNumber;
        this.rooms = rooms;
        this.commonRooms = commonRooms;
    }
}
