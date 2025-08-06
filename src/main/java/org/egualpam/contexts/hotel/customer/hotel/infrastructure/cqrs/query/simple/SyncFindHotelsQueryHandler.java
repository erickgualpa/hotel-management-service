package org.egualpam.contexts.hotel.customer.hotel.infrastructure.cqrs.query.simple;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotel.customer.hotel.application.query.FindHotels;
import org.egualpam.contexts.hotel.customer.hotel.application.query.FindHotelsQuery;
import org.egualpam.contexts.hotel.shared.application.query.ReadModel;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.Query;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.simple.QueryHandler;

@RequiredArgsConstructor
public class SyncFindHotelsQueryHandler implements QueryHandler {

  private final FindHotels findHotels;

  @Override
  public ReadModel handle(Query query) {
    FindHotelsQuery findHotelsQuery =
        Optional.of(query)
            .filter(SyncFindHotelsQuery.class::isInstance)
            .map(SyncFindHotelsQuery.class::cast)
            .map(SyncFindHotelsQueryHandler::toApplicationQuery)
            .orElseThrow();
    return findHotels.execute(findHotelsQuery);
  }

  private static FindHotelsQuery toApplicationQuery(SyncFindHotelsQuery q) {
    return new FindHotelsQuery(q.location(), q.minPrice(), q.maxPrice());
  }
}
