package org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.simple;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotelmanagement.e2e.models.PublicEventResult;
import org.egualpam.services.hotelmanagement.reviews.domain.ReviewCreated;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.services.hotelmanagement.shared.domain.EventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.services.hotelmanagement.shared.infrastructure.helpers.EventStoreTestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
class SimpleEventBusIT extends AbstractIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EventStoreTestRepository eventStoreTestRepository;

    private EventBus eventBus;

    @BeforeEach
    void setUp() {
        eventBus = new SimpleEventBus(entityManager);
    }

    @Test
    void publishDomainEvents() {
        String aggregateId = UUID.randomUUID().toString();
        Instant occurredOn = Instant.now();
        DomainEvent domainEvent = new ReviewCreated(
                new AggregateId(aggregateId),
                occurredOn
        );

        eventBus.publish(List.of(domainEvent));

        PublicEventResult result = eventStoreTestRepository.findEvent(domainEvent.getId().value());
        assertThat(result).satisfies(r -> {
            assertThat(r.type()).isEqualTo("hotelmanagement.reviews.created.v1.0");
            assertThat(r.aggregateId()).isEqualTo(aggregateId);
            assertNotNull(r.occurredOn());
        });
    }
}