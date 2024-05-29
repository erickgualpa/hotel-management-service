package org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.simple;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotelmanagement.reviews.domain.ReviewCreated;
import org.egualpam.services.hotelmanagement.reviews.domain.ReviewUpdated;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.events.PublicEvent;
import org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.events.ReviewCreatedPublicEvent;
import org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.events.ReviewUpdatedPublicEvent;

import java.util.List;

public class SimplePublicEventBus implements PublicEventBus {

    private static final String INSERT_INTO_EVENT_STORE = """
                INSERT INTO event_store(aggregate_id, occurred_on, event_type)
                VALUES (:aggregateId, :occurredOn, :eventType)
            """;

    private final EntityManager entityManager;

    public SimplePublicEventBus(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public void publish(List<DomainEvent> events) {
        events.forEach(this::persistEvent);
    }

    private int persistEvent(DomainEvent e) {
        // TODO: This code is duplicated
        PublicEvent publicEvent;
        if (e instanceof ReviewCreated) {
            publicEvent = new ReviewCreatedPublicEvent(
                    e.getId().toString(),
                    e.getAggregateId().value(),
                    e.getOccurredOn()
            );
        } else if (e instanceof ReviewUpdated) {
            publicEvent = new ReviewUpdatedPublicEvent(
                    e.getId().toString(),
                    e.getAggregateId().value(),
                    e.getOccurredOn()
            );
        } else {
            // TODO: Consider using a custom exception
            throw new RuntimeException("Unsupported domain event getType");
        }
        return entityManager.createNativeQuery(INSERT_INTO_EVENT_STORE)
                // TODO: Add misssing 'id' parameter
                .setParameter("aggregateId", publicEvent.getAggregateId())
                .setParameter("occurredOn", publicEvent.getOccurredOn())
                .setParameter("eventType", publicEvent.getType())
                .executeUpdate();
    }
}
