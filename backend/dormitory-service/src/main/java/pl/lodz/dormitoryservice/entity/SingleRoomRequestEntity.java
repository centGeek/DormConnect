package pl.lodz.dormitoryservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "single_room_requests")
public class SingleRoomRequestEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    // Obecny pokój, z którego student chce się przenieść
    @Column(nullable = false)
    private Long currentRoomId;
//
    // Preferencje (opcjonalne)
    private boolean onlySingleRoom; // czy zgadza się tylko na całkowicie jednoosobowy pokój

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime respondedAt;

    public enum RequestStatus {
        PENDING,
        ACCEPTED,
        REJECTED,
        CANCELLED
    }
}