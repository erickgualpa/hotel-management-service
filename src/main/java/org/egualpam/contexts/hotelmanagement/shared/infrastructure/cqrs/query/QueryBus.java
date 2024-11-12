package org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query;

import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModel;

@FunctionalInterface
public interface QueryBus {
  ReadModel publish(Query query);
}
