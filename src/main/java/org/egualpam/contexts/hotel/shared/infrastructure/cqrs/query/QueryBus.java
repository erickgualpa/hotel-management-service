package org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query;

import org.egualpam.contexts.hotel.shared.application.query.ReadModel;

@FunctionalInterface
public interface QueryBus {
  ReadModel publish(Query query);
}
