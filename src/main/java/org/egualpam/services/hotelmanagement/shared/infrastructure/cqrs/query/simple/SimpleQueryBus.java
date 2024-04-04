package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import org.egualpam.services.hotelmanagement.shared.application.query.Query;
import org.egualpam.services.hotelmanagement.shared.application.query.QueryBus;
import org.egualpam.services.hotelmanagement.shared.application.query.View;

import java.util.Map;
import java.util.Optional;

public final class SimpleQueryBus implements QueryBus {

    private final Map<Class<? extends Query>, QueryHandler> queryHandlers;

    public SimpleQueryBus(Map<Class<? extends Query>, QueryHandler> queryHandlers) {
        this.queryHandlers = queryHandlers;
    }

    @Override
    public View publish(Query query) {
        return Optional.ofNullable(queryHandlers.get(query.getClass()))
                .orElseThrow(QueryHandlerNotFound::new)
                .handle(query);
    }
}
