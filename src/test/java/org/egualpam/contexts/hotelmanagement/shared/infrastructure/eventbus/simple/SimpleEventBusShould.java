package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.simple;

import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.UnsupportedDomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleEventBusShould {

  @Mock private EntityManager entityManager;

  private EventBus eventBus;

  @BeforeEach
  void setUp() {
    eventBus = new SimpleEventBus(entityManager);
  }

  @Test
  void throwException_whenDomainEventIsUnsupported() {
    DomainEvent domainEvent =
        new DomainEvent() {
          @Override
          public UniqueId id() {
            return UniqueId.get();
          }

          @Override
          public AggregateId aggregateId() {
            return new AggregateId(UUID.randomUUID().toString());
          }

          @Override
          public Instant occurredOn() {
            return Instant.now();
          }
        };

    List<DomainEvent> events = List.of(domainEvent);
    assertThrows(UnsupportedDomainEvent.class, () -> eventBus.publish(events));
  }
}
