package org.egualpam.contexts.hotelmanagement.room.infrastructure.cqrs.query;

import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.room.application.query.FindRoomNextMonthAvailability;
import org.egualpam.contexts.hotelmanagement.room.application.query.FindRoomNextMonthAvailabilityQuery;
import org.egualpam.contexts.hotelmanagement.room.infrastructure.cqrs.query.simple.SyncFindRoomNextMonthAvailabilityQuery;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModel;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;

public class SyncFindRoomNextMonthAvailabilityHandler implements QueryHandler {

  private final FindRoomNextMonthAvailability findRoomNextMonthAvailability;

  public SyncFindRoomNextMonthAvailabilityHandler(
      FindRoomNextMonthAvailability findRoomNextMonthAvailability) {
    this.findRoomNextMonthAvailability = findRoomNextMonthAvailability;
  }

  private static FindRoomNextMonthAvailabilityQuery toApplicationQuery(
      SyncFindRoomNextMonthAvailabilityQuery query) {
    return new FindRoomNextMonthAvailabilityQuery(query.roomId());
  }

  @Override
  public ReadModel handle(Query query) {
    return Optional.of(query)
        .filter(SyncFindRoomNextMonthAvailabilityQuery.class::isInstance)
        .map(SyncFindRoomNextMonthAvailabilityQuery.class::cast)
        .map(SyncFindRoomNextMonthAvailabilityHandler::toApplicationQuery)
        .map(findRoomNextMonthAvailability::execute)
        .orElseThrow();
  }
}
