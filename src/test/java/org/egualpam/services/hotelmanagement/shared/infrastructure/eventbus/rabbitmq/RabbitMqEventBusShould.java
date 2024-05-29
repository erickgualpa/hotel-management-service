package org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.services.hotelmanagement.shared.domain.EventBus;
import org.egualpam.services.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.services.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.services.hotelmanagement.shared.infrastructure.configuration.properties.eventbus.RabbitMqProperties;
import org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.events.UnsupportedDomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RabbitMqEventBusShould extends AbstractIntegrationTest {

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Autowired
    private ObjectMapper objectMapper;

    private EventBus eventBus;

    @BeforeEach
    void setUp() {
        eventBus = new RabbitMqEventBus(rabbitMqProperties, objectMapper);
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