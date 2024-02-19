package org.egualpam.services.hotel.rating.infrastructure.events.publishers.simple;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotel.rating.domain.shared.DomainEvent;
import org.egualpam.services.hotel.rating.domain.shared.DomainEventsPublisher;

import java.util.List;
import java.util.UUID;

public class SimpleDomainEventsPublisher implements DomainEventsPublisher {

    private final EntityManager entityManager;

    public SimpleDomainEventsPublisher(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public void publish(List<DomainEvent> events) {
        String sql = """
                    INSERT INTO event_store(aggregate_id, event_type)
                    VALUES (:aggregateId, :eventType)
                """;
        events.forEach(
                e -> entityManager.createNativeQuery(sql)
                        .setParameter("aggregateId", UUID.fromString(e.getAggregateId().value()))
                        .setParameter("eventType", e.getType())
                        .executeUpdate()
        );
    }
}
