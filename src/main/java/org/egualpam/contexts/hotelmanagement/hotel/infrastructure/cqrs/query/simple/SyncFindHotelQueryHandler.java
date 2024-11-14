package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotel;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotelQuery;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModel;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;

@RequiredArgsConstructor
public class SyncFindHotelQueryHandler implements QueryHandler {

  private final FindHotel findHotel;

  @Override
  public ReadModel handle(Query query) {
    FindHotelQuery findHotelQuery =
        Optional.of(query)
            .filter(SyncFindHotelQuery.class::isInstance)
            .map(SyncFindHotelQuery.class::cast)
            .map(q -> new FindHotelQuery(q.hotelId()))
            .orElseThrow();
    return findHotel.execute(findHotelQuery);
  }
}
