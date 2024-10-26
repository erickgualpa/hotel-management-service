package org.egualpam.contexts.hotelmanagement.shared.application.query;

@FunctionalInterface
public interface QueryBus {
    View publish(Query query);
}
