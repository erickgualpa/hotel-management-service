package org.egualpam.contexts.hotel.shared.infrastructure.eventbus.springamqp;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import org.egualpam.contexts.AbstractIntegrationTest;
import org.egualpam.contexts.hotel.management.hotel.domain.HotelCreated;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.DomainEvent;
import org.egualpam.contexts.hotel.shared.domain.EventBus;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;
import org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared.EventStoreRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.EventStoreTestRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.PublicEventResult;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.RabbitMqTestConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class SpringAmqpEventBusIT extends AbstractIntegrationTest {

  private static final Instant NOW = Instant.parse("2025-01-22T14:54:04.954430Z");

  @Mock private Clock clock;

  @Autowired private ObjectMapper objectMapper;
  @Autowired private JsonSchemaFactory jsonSchemaFactory;
  @Autowired private EventStoreRepository eventStoreRepository;
  @Autowired private RabbitTemplate rabbitTemplate;
  @Autowired private RabbitMqTestConsumer rabbitMqTestConsumer;
  @Autowired private EventStoreTestRepository eventStoreTestRepository;

  private EventBus testSubject;

  @BeforeEach
  void setUp() {
    testSubject =
        new SpringAmqpEventBus(
            objectMapper, jsonSchemaFactory, eventStoreRepository, rabbitTemplate);
  }

  @Test
  void publishDomainEvents() {
    when(clock.instant()).thenReturn(NOW);

    UniqueId eventId = UniqueId.get();
    AggregateId aggregateId = new AggregateId(UniqueId.get().value());
    DomainEvent domainEvent = new HotelCreated(eventId, aggregateId, clock);

    testSubject.publish(Set.of(domainEvent));

    await()
        .atMost(10, SECONDS)
        .untilAsserted(
            () -> {
              // Event is saved in event store
              PublicEventResult eventStoreEvent =
                  eventStoreTestRepository.findEvent(eventId.value());
              assertThat(eventStoreEvent)
                  .satisfies(
                      r -> {
                        assertThat(r.id()).isEqualTo(eventId.value());
                        assertThat(r.type()).isEqualTo("hotelmanagement.hotel.created");
                        assertThat(r.version()).isEqualTo("1.0");
                        assertThat(r.aggregateId()).isEqualTo(aggregateId.value());
                        assertThat(r.occurredOn()).isEqualTo(NOW);
                      });

              // Event is published in queue
              PublicEventResult queueEvent =
                  rabbitMqTestConsumer.consumeFromQueue("hotelmanagement.test");
              assertThat(queueEvent)
                  .satisfies(
                      r -> {
                        assertThat(r.id()).isEqualTo(eventId.value());
                        assertThat(r.type()).isEqualTo("hotelmanagement.hotel.created");
                        assertThat(r.version()).isEqualTo("1.0");
                        assertThat(r.aggregateId()).isEqualTo(aggregateId.value());
                        assertThat(r.occurredOn()).isEqualTo(NOW);
                      });
            });
  }
}
