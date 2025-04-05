package pl.lodz.dormConnect.dorm.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

@Entity
public class RoomAssignEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RoomEntity rooms;
    //WARNING!
    //Idk if students entities are gonna be in my module, so for now i only put Id
    private Long studentId;

    @Column(nullable = false)
    LocalDate fromDate;

    //Ongoing "rentals" are gonna have endDate null
    @Column(nullable = true)
    private LocalDate toDate;


}
