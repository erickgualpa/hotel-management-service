package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import org.egualpam.services.hotelmanagement.shared.application.query.Query;
import org.egualpam.services.hotelmanagement.shared.application.query.View;

public interface QueryHandler {
    View handle(Query query);

    Class<? extends Query> type();
}
