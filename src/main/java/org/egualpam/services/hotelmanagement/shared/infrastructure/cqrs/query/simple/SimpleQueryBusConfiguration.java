package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import org.egualpam.services.hotelmanagement.shared.application.query.Query;

import java.util.HashMap;
import java.util.Map;

public final class SimpleQueryBusConfiguration {

    private final Map<Class<? extends Query>, QueryHandler> handlers = new HashMap<>();

    public SimpleQueryBusConfiguration withHandler(Class<? extends Query> type, QueryHandler handler) {
        handlers.put(type, handler);
        return this;
    }

    public Map<Class<? extends Query>, QueryHandler> getHandlers() {
        return handlers;
    }
}
