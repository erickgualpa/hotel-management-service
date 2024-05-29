package org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.simple;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.services.hotelmanagement.shared.domain.EventBus;
import org.egualpam.services.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.events.UnsupportedDomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class SimpleEventBusShould {

    @Mock
    private EntityManager entityManager;

    private EventBus eventBus;

    @BeforeEach
    void setUp() {
        eventBus = new SimpleEventBus(entityManager);
    }

    @Test
    void throwException_whenDomainEventIsUnsupported() {
        DomainEvent domainEvent = new DomainEvent() {
            @Override
            public UniqueId getId() {
                return UniqueId.get();
            }

            @Override
            public AggregateId getAggregateId() {
                return new AggregateId(UUID.randomUUID().toString());
            }

            @Override
            public Instant getOccurredOn() {
                return Instant.now();
            }
        };

        List<DomainEvent> events = List.of(domainEvent);
        assertThrows(UnsupportedDomainEvent.class, () -> eventBus.publish(events));
    }
}