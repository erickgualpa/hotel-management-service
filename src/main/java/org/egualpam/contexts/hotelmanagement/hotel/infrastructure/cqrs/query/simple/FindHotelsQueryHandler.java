package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotels;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotelsQuery;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModel;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;

@RequiredArgsConstructor
public class FindHotelsQueryHandler implements QueryHandler<FindHotelsQuery> {

  private final FindHotels findHotels;

  @Override
  public ReadModel handle(FindHotelsQuery query) {
    return findHotels.execute(query);
  }
}
