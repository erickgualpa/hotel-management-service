package org.egualpam.contexts.hotel.shared.infrastructure.eventbus.simple;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.DomainEvent;
import org.egualpam.contexts.hotel.shared.domain.EventBus;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;
import org.egualpam.contexts.hotel.shared.infrastructure.configuration.ObjectMapperConfiguration;
import org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared.UnsupportedDomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@ExtendWith(MockitoExtension.class)
class SimpleEventBusShould {

  private static final Instant NOW = Instant.now();

  @Mock private Clock clock;
  @Mock private JsonSchemaFactory jsonSchemaFactory;
  @Mock private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private final ObjectMapper objectMapper = new ObjectMapperConfiguration().objectMapper();

  private EventBus eventBus;

  @BeforeEach
  void setUp() {
    eventBus = new SimpleEventBus(objectMapper, jsonSchemaFactory, namedParameterJdbcTemplate);
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
