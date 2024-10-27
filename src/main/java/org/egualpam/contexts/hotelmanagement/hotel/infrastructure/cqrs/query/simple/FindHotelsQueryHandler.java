package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotelsQuery;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModel;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;

@RequiredArgsConstructor
public class FindHotelsQueryHandler implements QueryHandler {

  private final ReadModelSupplier<ManyHotels> readModelSupplier;

  @Override
  public ReadModel handle(Query query) {
    FindHotelsQuery findHotelsQuery = (FindHotelsQuery) query;
    return readModelSupplier.get(
        new HotelCriteria(
            findHotelsQuery.location(), findHotelsQuery.minPrice(), findHotelsQuery.maxPrice()));
  }
}
