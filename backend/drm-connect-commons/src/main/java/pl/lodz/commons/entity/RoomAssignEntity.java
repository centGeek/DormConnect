package pl.lodz.commons.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RoomAssignEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RoomEntity room;
    //WARNING!
    //Idk if students entities are gonna be in my module, so for now i only put Id
    private Long residentId;

    @Column(nullable = false)
    LocalDate fromDate;

    //Ongoing "rentals" are gonna have endDate null
    @Column(nullable = true)
    private LocalDate toDate;

    public void setRoom(RoomEntity room) {
        this.room = room;
        room.getRoomAssigns().add(this);
    }
}
