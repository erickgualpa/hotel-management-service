package org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModel;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.Query;

@FunctionalInterface
public interface QueryHandler<T extends Query> {
  ReadModel handle(T query);
}
