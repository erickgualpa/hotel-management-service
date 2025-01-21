package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.UnsupportedDomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared.EventStoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RabbitMqEventBusShould {

  private static final Instant NOW = Instant.now();

  @Mock private Clock clock;
  @Mock private Channel channel;
  @Mock private ObjectMapper objectMapper;
  @Mock private EventStoreRepository eventStoreRepository;

  private EventBus eventBus;

  @BeforeEach
  void setUp() {
    eventBus = new RabbitMqEventBus(channel, objectMapper, eventStoreRepository);
  }

  @Test
  void throwException_whenDomainEventIsUnsupported() {
    when(clock.instant()).thenReturn(NOW);

    AggregateId aggregateId = new AggregateId(UniqueId.get().value());
    DomainEvent domainEvent = new DomainEvent(UniqueId.get(), aggregateId, clock) {};
    Set<DomainEvent> events = Set.of(domainEvent);

    assertThrows(UnsupportedDomainEvent.class, () -> eventBus.publish(events));
  }
}
