package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Connection;
import java.util.Set;
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
class RabbitMqEventBusShould {

  @Mock private Connection connection;

  @Mock private ObjectMapper objectMapper;

  private EventBus eventBus;

  @BeforeEach
  void setUp() {
    eventBus = new RabbitMqEventBus(connection, objectMapper);
  }

  @Test
  void throwException_whenDomainEventIsUnsupported() {
    AggregateId aggregateId = new AggregateId(UniqueId.get().value());
    DomainEvent domainEvent = new DomainEvent(aggregateId) {};
    Set<DomainEvent> events = Set.of(domainEvent);
    assertThrows(UnsupportedDomainEvent.class, () -> eventBus.publish(events));
  }
}
