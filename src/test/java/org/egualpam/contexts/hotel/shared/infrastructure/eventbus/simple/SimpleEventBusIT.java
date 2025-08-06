package org.egualpam.contexts.hotel.shared.infrastructure.eventbus.simple;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import org.egualpam.contexts.hotel.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotel.customer.review.domain.HotelId;
import org.egualpam.contexts.hotel.customer.review.domain.Rating;
import org.egualpam.contexts.hotel.customer.review.domain.ReviewCreated;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.DomainEvent;
import org.egualpam.contexts.hotel.shared.domain.EventBus;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.EventStoreTestRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.PublicEventResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

class SimpleEventBusIT extends AbstractIntegrationTest {

  private static final Instant NOW = Instant.parse("2025-01-22T14:54:04.954430Z");

  @Mock private Clock clock;

  @Autowired private ObjectMapper objectMapper;
  @Autowired private JsonSchemaFactory jsonSchemaFactory;
  @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Autowired private EventStoreTestRepository eventStoreTestRepository;

  private EventBus eventBus;

  @BeforeEach
  void setUp() {
    eventBus = new SimpleEventBus(objectMapper, jsonSchemaFactory, namedParameterJdbcTemplate);
  }

  @Test
  void publishDomainEvents() {
    when(clock.instant()).thenReturn(NOW);

    String aggregateId = UniqueId.get().value();
    HotelId hotelId = new HotelId(UniqueId.get().value());
    Rating rating = new Rating(3);

    // TODO: Try to publish all existing extensions of 'DomainEvent'
    DomainEvent domainEvent =
        new ReviewCreated(UniqueId.get(), new AggregateId(aggregateId), hotelId, rating, clock);

    eventBus.publish(Set.of(domainEvent));

    PublicEventResult result = eventStoreTestRepository.findEvent(domainEvent.id().value());
    assertThat(result)
        .satisfies(
            r -> {
              assertThat(r.type()).isEqualTo("hotelmanagement.review.created");
              assertThat(r.version()).isEqualTo("1.0");
              assertThat(r.aggregateId()).isEqualTo(aggregateId);
              assertThat(r.occurredOn()).isEqualTo(NOW);
            });
  }
}
