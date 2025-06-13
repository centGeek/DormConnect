package pl.lodz.commons.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "event")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @Column(name = "location")
    private String location;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "organizer_id")
    private Long organizerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false, columnDefinition = "varchar(100) default 'WAITING'")
    private ApprovalStatus approvalStatus = ApprovalStatus.WAITING;

    @ElementCollection
    @CollectionTable(name = "event_participant", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "participant_id")
    private List<Long> participantId;
}
