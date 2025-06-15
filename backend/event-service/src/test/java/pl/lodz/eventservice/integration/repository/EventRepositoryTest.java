package pl.lodz.eventservice.integration.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.eventservice.config.PostgresContainerConfig;
import pl.lodz.eventservice.entity.ApprovalStatus;
import pl.lodz.eventservice.entity.EventEntity;
import pl.lodz.eventservice.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import(PostgresContainerConfig.class)
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    private EventEntity createEvent(String name, ApprovalStatus status, Long organizerId, List<Long> participants, LocalDateTime startDate) {
        return eventRepository.save(EventEntity.builder()
                .eventName(name)
                .description("Test description")
                .startDateTime(startDate)
                .endDateTime(startDate.plusHours(2))
                .location("Test Location")
                .eventType("Test Type")
                .maxParticipants(50)
                .imageUrl("http://example.com/image.jpg")
                .organizerId(organizerId)
                .approvalStatus(status)
                .participantId(participants)
                .build());
    }

    @Test
    @DisplayName("should return all events")
    void shouldFindAll() {
        createEvent("Event 1", ApprovalStatus.APPROVED, 1L, List.of(10L), LocalDateTime.now().plusDays(1));
        Page<EventEntity> page = eventRepository.findAll(PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isGreaterThan(0);
    }

    @Test
    @DisplayName("should find events by future date")
    void shouldFindAllByStartDateTimeAfter() {
        createEvent("Future Event", ApprovalStatus.APPROVED, 2L, List.of(), LocalDateTime.now().plusDays(2));
        Page<EventEntity> page = eventRepository.findAllByStartDateTimeAfter(LocalDateTime.now(), PageRequest.of(0, 10));
        assertThat(page.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("should find by approval and start date")
    void shouldFindByApprovalStatusAndStartDateTimeAfter() {
        createEvent("Future Approved", ApprovalStatus.APPROVED, 2L, List.of(), LocalDateTime.now().plusDays(2));
        Page<EventEntity> page = eventRepository.findAllByApprovalStatusAndStartDateTimeAfter(
                ApprovalStatus.APPROVED, LocalDateTime.now(), PageRequest.of(0, 10));
        assertThat(page.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("should find events containing participant")
    void shouldFindByParticipantIdContaining() {
        createEvent("With participant", ApprovalStatus.APPROVED, 3L, List.of(123L), LocalDateTime.now().plusDays(1));
        Page<EventEntity> page = eventRepository.findByParticipantIdContaining(123L, PageRequest.of(0, 10));
        assertThat(page.getContent()).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("should find events by organizer and start date")
    void shouldFindByOrganizerIdAndStartDateTimeAfter() {
        createEvent("Organizer future", ApprovalStatus.APPROVED, 42L, List.of(), LocalDateTime.now().plusDays(3));
        Page<EventEntity> page = eventRepository.findByOrganizerIdAndStartDateTimeAfter(
                42L, PageRequest.of(0, 10), LocalDateTime.now());
        assertThat(page.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("should check if any event exists by organizer")
    void shouldExistByOrganizerId() {
        createEvent("Organizer check", ApprovalStatus.WAITING, 99L, List.of(), LocalDateTime.now().plusDays(1));
        boolean exists = eventRepository.existsByOrganizerId(99L);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("should find by approval status")
    void shouldFindByApprovalStatus() {
        createEvent("Rejected event", ApprovalStatus.DECLINED, 11L, List.of(), LocalDateTime.now().plusDays(1));
        Page<EventEntity> page = eventRepository.findByApprovalStatus(ApprovalStatus.DECLINED, PageRequest.of(0, 10));
        assertThat(page.getContent()).isNotEmpty();
    }
}
