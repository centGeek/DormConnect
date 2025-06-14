package pl.lodz.dormitoryservice.entity;

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

    private Long residentId;

    @Column(nullable = false)
    LocalDate fromDate;

    @Column
    private LocalDate toDate;

    public void setRoom(RoomEntity room) {
        this.room = room;
        room.getRoomAssigns().add(this);
    }

    public RoomAssignEntity(RoomEntity room, Long residentId, LocalDate fromDate, LocalDate toDate) {
        this.room = room;
        this.residentId = residentId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}
