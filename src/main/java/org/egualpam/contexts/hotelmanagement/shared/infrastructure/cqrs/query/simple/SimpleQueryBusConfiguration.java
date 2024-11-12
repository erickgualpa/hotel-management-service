package org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import java.util.HashMap;
import java.util.Map;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.Query;

public final class SimpleQueryBusConfiguration {

  private final Map<Class<? extends Query>, QueryHandler<? extends Query>> handlers =
      new HashMap<>();

  public SimpleQueryBusConfiguration withHandler(
      Class<? extends Query> type, QueryHandler<? extends Query> handler) {
    handlers.put(type, handler);
    return this;
  }

  public Map<Class<? extends Query>, QueryHandler<? extends Query>> getHandlers() {
    return handlers;
  }
}
