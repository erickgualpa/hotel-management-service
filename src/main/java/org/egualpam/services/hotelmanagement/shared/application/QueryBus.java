package org.egualpam.services.hotelmanagement.shared.application;

@FunctionalInterface
public interface QueryBus {
    View publish(Query query);
}
