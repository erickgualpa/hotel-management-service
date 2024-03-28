package org.egualpam.services.hotelmanagement.shared.application.query;

@FunctionalInterface
public interface QueryBus {
    View publish(Query query);
}
