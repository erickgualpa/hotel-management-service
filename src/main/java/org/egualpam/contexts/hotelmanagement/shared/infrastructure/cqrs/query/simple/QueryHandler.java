package org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModel;

@FunctionalInterface
public interface QueryHandler<T extends Query> {
  ReadModel handle(T query);
}
