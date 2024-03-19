package org.egualpam.services.hotelmanagement.application.shared;

@FunctionalInterface
public interface QueryBus {
    View publish(Query query);
}
