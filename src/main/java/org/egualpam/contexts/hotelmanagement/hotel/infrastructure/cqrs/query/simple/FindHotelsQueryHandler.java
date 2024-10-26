package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotelsQuery;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.MultipleHotelsView;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.application.query.View;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;

@RequiredArgsConstructor
public class FindHotelsQueryHandler implements QueryHandler {

  private final ReadModelSupplier<MultipleHotelsView> multipleHotelsReadModelSupplier;

  @Override
  public View handle(Query query) {
    FindHotelsQuery findHotelsQuery = (FindHotelsQuery) query;
    return multipleHotelsReadModelSupplier.get(
        new HotelCriteria(
            findHotelsQuery.getLocation().orElse(null),
            findHotelsQuery.getMinPrice().orElse(null),
            findHotelsQuery.getMaxPrice().orElse(null)));
  }
}
