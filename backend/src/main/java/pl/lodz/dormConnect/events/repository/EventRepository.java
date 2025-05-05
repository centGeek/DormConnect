package pl.lodz.dormConnect.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.dormConnect.events.model.EventEntity;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    Page<EventEntity> findAll(Pageable pageable);

    Page<EventEntity> findAllByIsApprovedIsTrueAndStartDateTimeAfter(Pageable pageable, LocalDateTime dateTime);

    Page<EventEntity> findByParticipantIdContaining(Long participantId, Pageable pageable);

    Page<EventEntity> findByOrganizerIdAndStartDateTimeAfter(Long organizerId, Pageable pageable, LocalDateTime dateTime);

    boolean existsByOrganizerId(Long organizerId);
}
