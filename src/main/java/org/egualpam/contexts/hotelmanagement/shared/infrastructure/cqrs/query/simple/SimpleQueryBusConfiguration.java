package org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import java.util.HashMap;
import java.util.Map;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.Query;

public final class SimpleQueryBusConfiguration {

  private final Map<Class<? extends Query>, QueryHandler> handlers = new HashMap<>();

  public SimpleQueryBusConfiguration withHandler(
      Class<? extends Query> type, QueryHandler handler) {
    handlers.put(type, handler);
    return this;
  }

  public Map<Class<? extends Query>, QueryHandler> getHandlers() {
    return handlers;
  }
}
