package org.egualpam.contexts.hotelmanagement.room.infrastructure.cqrs.query;

import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.room.application.query.FindRooms;
import org.egualpam.contexts.hotelmanagement.room.application.query.FindRoomsQuery;
import org.egualpam.contexts.hotelmanagement.room.infrastructure.cqrs.query.simple.SyncFindRoomsQuery;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModel;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;

public class SyncFindRoomsHandler implements QueryHandler {

  private final FindRooms findRooms;

  public SyncFindRoomsHandler(FindRooms findRooms) {
    this.findRooms = findRooms;
  }

  private static FindRoomsQuery toApplicationQuery(SyncFindRoomsQuery query) {
    return new FindRoomsQuery(query.availableFrom(), query.availableTo());
  }

  @Override
  public ReadModel handle(Query query) {
    return Optional.of(query)
        .filter(SyncFindRoomsQuery.class::isInstance)
        .map(SyncFindRoomsQuery.class::cast)
        .map(SyncFindRoomsHandler::toApplicationQuery)
        .map(findRooms::execute)
        .orElseThrow();
  }
}
