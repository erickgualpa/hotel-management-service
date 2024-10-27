package org.egualpam.contexts.hotelmanagement.shared.application.query;

@FunctionalInterface
public interface QueryBus {
  ReadModel publish(Query query);
}
