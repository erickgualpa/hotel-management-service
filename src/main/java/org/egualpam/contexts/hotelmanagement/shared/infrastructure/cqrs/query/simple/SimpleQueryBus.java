package org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.application.query.QueryBus;
import org.egualpam.contexts.hotelmanagement.shared.application.query.View;

import java.util.Map;
import java.util.Optional;

public final class SimpleQueryBus implements QueryBus {

    private final Map<Class<? extends Query>, QueryHandler> handlers;

    public SimpleQueryBus(Map<Class<? extends Query>, QueryHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public View publish(Query query) {
        return Optional.ofNullable(handlers.get(query.getClass()))
                .orElseThrow(QueryHandlerNotFound::new)
                .handle(query);
    }
}
