package pl.lodz.eventservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.eventservice.entity.ApprovalStatus;
import pl.lodz.eventservice.entity.EventEntity;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    Page<EventEntity> findAll(Pageable pageable);

    Page<EventEntity> findAllByStartDateTimeAfter(LocalDateTime dateTime, Pageable pageable);

    Page<EventEntity> findAllByApprovalStatusAndStartDateTimeAfter(ApprovalStatus status, LocalDateTime dateTime, Pageable pageable);

    Page<EventEntity> findByParticipantIdContaining(Long participantId, Pageable pageable);

    Page<EventEntity> findByOrganizerIdAndStartDateTimeAfter(Long organizerId, Pageable pageable, LocalDateTime dateTime);

    boolean existsByOrganizerId(Long organizerId);

    Page<EventEntity> findByApprovalStatus(ApprovalStatus approvalStatus, Pageable pageable);
}
