package org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotel.shared.application.query.ReadModel;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.Query;

@FunctionalInterface
public interface QueryHandler {
  ReadModel handle(Query query);
}
