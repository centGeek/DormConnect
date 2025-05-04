package pl.lodz.dormConnect.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.dormConnect.events.model.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    Page<EventEntity> findAll(Pageable pageable);

    Page<EventEntity> findAllByIsApprovedIsTrue(Pageable pageable);

    Page<EventEntity> findByParticipantIdContaining(Long participantId, Pageable pageable);

    Page<EventEntity> findByOrganizerId(Long organizerId, Pageable pageable);

    boolean existsByOrganizerId(Long organizerId);
}
