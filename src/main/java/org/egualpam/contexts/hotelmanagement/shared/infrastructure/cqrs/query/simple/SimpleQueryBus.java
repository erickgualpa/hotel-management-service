package org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import java.util.Map;
import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModel;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.QueryBus;

public final class SimpleQueryBus implements QueryBus {

  private final Map<Class<? extends Query>, QueryHandler> handlers;

  public SimpleQueryBus(Map<Class<? extends Query>, QueryHandler> handlers) {
    this.handlers = handlers;
  }

  @Override
  public ReadModel publish(Query query) {
    return Optional.ofNullable(handlers.get(query.getClass()))
        .orElseThrow(QueryHandlerNotFound::new)
        .handle(query);
  }
}
