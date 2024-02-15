package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import org.egualpam.services.hotel.rating.application.shared.Query;

public interface QueryBus {
    <T> T publish(Query<T> query);
}
