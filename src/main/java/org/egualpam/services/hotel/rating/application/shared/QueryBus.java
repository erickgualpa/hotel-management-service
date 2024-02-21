package org.egualpam.services.hotel.rating.application.shared;

@FunctionalInterface
public interface QueryBus {
    View publish(Query query);
}
