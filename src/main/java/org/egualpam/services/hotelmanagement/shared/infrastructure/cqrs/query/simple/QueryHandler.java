package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import org.egualpam.services.hotelmanagement.shared.application.query.Query;
import org.egualpam.services.hotelmanagement.shared.application.query.View;

@FunctionalInterface
public interface QueryHandler<T extends Query> {
    View handle(T query);
}
