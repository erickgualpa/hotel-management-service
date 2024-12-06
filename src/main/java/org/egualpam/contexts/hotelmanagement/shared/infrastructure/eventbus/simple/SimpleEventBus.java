package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.simple;

import jakarta.persistence.EntityManager;
import java.util.Set;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEventFactory;

public class SimpleEventBus implements EventBus {

  private static final String INSERT_INTO_EVENT_STORE =
      """
      INSERT INTO event_store(id, aggregate_id, occurred_on, event_type, event_version)
      VALUES (:id, :aggregateId, :occurredOn, :eventType, :eventVersion)
      """;

  private final EntityManager entityManager;

  public SimpleEventBus(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void publish(Set<DomainEvent> events) {
    events.forEach(this::persistEvent);
  }

  private void persistEvent(DomainEvent domainEvent) {
    PublicEvent publicEvent = PublicEventFactory.from(domainEvent);
    entityManager
        .createNativeQuery(INSERT_INTO_EVENT_STORE)
        .setParameter("id", UUID.fromString(publicEvent.getId()))
        .setParameter("aggregateId", UUID.fromString(publicEvent.getAggregateId()))
        .setParameter("occurredOn", publicEvent.getOccurredOn())
        .setParameter("eventType", publicEvent.getType())
        .setParameter("eventVersion", publicEvent.getVersion())
        .executeUpdate();
  }
}
