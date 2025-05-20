package pl.lodz.dormConnect.floors.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;


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

}
