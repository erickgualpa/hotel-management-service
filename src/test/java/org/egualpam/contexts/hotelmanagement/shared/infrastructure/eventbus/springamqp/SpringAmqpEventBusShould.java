package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.springamqp;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.configuration.ObjectMapperConfiguration;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.UnsupportedDomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared.EventStoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
class SpringAmqpEventBusShould {

  private static final Instant NOW = Instant.now();

  private final ObjectMapper objectMapper = new ObjectMapperConfiguration().objectMapper();

  @Mock private JsonSchemaFactory jsonSchemaFactory;
  @Mock private Clock clock;
  @Mock private EventStoreRepository eventStoreRepository;
  @Mock private RabbitTemplate rabbitTemplate;

  private EventBus testee;

  @BeforeEach
  void setUp() {
    testee =
        new SpringAmqpEventBus(
            objectMapper, jsonSchemaFactory, eventStoreRepository, rabbitTemplate);
  }

  @Test
  void throwException_whenDomainEventIsUnsupported() {
    when(clock.instant()).thenReturn(NOW);

    AggregateId aggregateId = new AggregateId(UniqueId.get().value());
    DomainEvent domainEvent = new DomainEvent(UniqueId.get(), aggregateId, clock) {};
    Set<DomainEvent> events = Set.of(domainEvent);

    assertThrows(UnsupportedDomainEvent.class, () -> testee.publish(events));
  }
}
