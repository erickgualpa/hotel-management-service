package org.egualpam.services.hotelmanagement.infrastructure.events.publishers.simple;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotelmanagement.domain.shared.DomainEvent;
import org.egualpam.services.hotelmanagement.domain.shared.DomainEventsBus;

import java.util.List;

public class SimpleDomainEventsBus implements DomainEventsBus {

    private final EntityManager entityManager;

    public SimpleDomainEventsBus(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public void publish(List<DomainEvent> events) {
        String sql = """
                    INSERT INTO event_store(aggregate_id, occurred_on, event_type)
                    VALUES (:aggregateId, :occurredOn, :eventType)
                """;
        events.forEach(
                e -> entityManager.createNativeQuery(sql)
                        .setParameter("aggregateId", e.getAggregateId().value())
                        .setParameter("occurredOn", e.getOccurredOn())
                        .setParameter("eventType", e.getType())
                        .executeUpdate()
        );
    }
}
