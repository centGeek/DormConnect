package pl.lodz.dormitoryservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "room_replacement_forms")
public class RoomReplacementFormEntity {

    //Ids are used instead of entities cause it s easier
    // to check it before and after then having a fight with circular dependencies...
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long requesterId;

    private Long targetStudent;

    // Pokój, z którego wychodzi requester
    @JoinColumn(name = "requester_room_id")
    private Long requesterRoom;

    // Pokój, z którego wychodzi targetStudent
    @JoinColumn(name = "target_room_id")
    private Long targetRoom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormStatus status = FormStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime respondedAt;

    public enum FormStatus {
        PENDING,
        ACCEPTED,
        REJECTED,
        CANCELLED
    }
}