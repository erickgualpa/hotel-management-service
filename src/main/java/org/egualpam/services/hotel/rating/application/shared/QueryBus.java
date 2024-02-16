package org.egualpam.services.hotel.rating.application.shared;

@FunctionalInterface
public interface QueryBus {
    String publish(Query query);
}
