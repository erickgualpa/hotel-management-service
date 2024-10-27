package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.simple;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.e2e.models.PublicEventResult;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCreated;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.EventStoreTestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
class SimpleEventBusIT extends AbstractIntegrationTest {

  @Autowired private EntityManager entityManager;

  @Autowired private EventStoreTestRepository eventStoreTestRepository;

  private EventBus eventBus;

  @BeforeEach
  void setUp() {
    eventBus = new SimpleEventBus(entityManager);
  }

  @Test
  void publishDomainEvents() {
    String aggregateId = UUID.randomUUID().toString();
    DomainEvent domainEvent = new ReviewCreated(new AggregateId(aggregateId));

    eventBus.publish(List.of(domainEvent));

    PublicEventResult result = eventStoreTestRepository.findEvent(domainEvent.id().value());
    assertThat(result)
        .satisfies(
            r -> {
              assertThat(r.type()).isEqualTo("hotelmanagement.reviews.created.v1.0");
              assertThat(r.aggregateId()).isEqualTo(aggregateId);
              assertNotNull(r.occurredOn());
            });
  }
}
