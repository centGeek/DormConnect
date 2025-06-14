package pl.lodz.dormitoryservice.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "dorm_rooms")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String number;
    private int capacity;
    private int floor;
    private boolean active = true;
    @OneToMany
    private List<RoomAssignEntity> roomAssigns = new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = true)
    //null means there is no grouping
    private GroupedRoomsEntity groupedRooms;



}
