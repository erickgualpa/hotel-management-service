package org.egualpam.services.hotel.rating.infrastructure.cqrs;

@FunctionalInterface
public interface QueryBus {
    String publish(Query query);
}
