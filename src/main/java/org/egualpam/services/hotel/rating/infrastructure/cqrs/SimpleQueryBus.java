package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import org.egualpam.services.hotel.rating.application.shared.Query;

public class SimpleQueryBus implements QueryBus {
    @Override
    public <T> T publish(Query<T> query) {
        return query.get();
    }
}
