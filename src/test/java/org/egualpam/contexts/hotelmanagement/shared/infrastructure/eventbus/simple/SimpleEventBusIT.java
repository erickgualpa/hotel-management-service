package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.simple;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.time.Clock;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.review.domain.HotelId;
import org.egualpam.contexts.hotelmanagement.review.domain.Rating;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCreated;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.EventStoreTestRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.PublicEventResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Transactional
class SimpleEventBusIT extends AbstractIntegrationTest {

  @Autowired private Clock clock;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Autowired private EventStoreTestRepository eventStoreTestRepository;

  private EventBus eventBus;

  @BeforeEach
  void setUp() {
    eventBus = new SimpleEventBus(objectMapper, namedParameterJdbcTemplate);
  }

  @Test
  void publishDomainEvents() {
    String aggregateId = UniqueId.get().value();
    HotelId hotelId = new HotelId(UniqueId.get().value());
    Rating rating = new Rating(3);
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
              assertNotNull(r.occurredOn());
            });
  }
}
