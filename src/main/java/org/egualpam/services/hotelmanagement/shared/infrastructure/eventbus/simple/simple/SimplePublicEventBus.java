package org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.simple.simple;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;

import java.util.List;

public class SimplePublicEventBus implements PublicEventBus {

    private final EntityManager entityManager;

    public SimplePublicEventBus(EntityManager entityManager) {
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
