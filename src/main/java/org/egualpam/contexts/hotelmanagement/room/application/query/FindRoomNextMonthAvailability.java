package org.egualpam.contexts.hotelmanagement.room.application.query;

import org.egualpam.contexts.hotelmanagement.room.domain.RoomCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;

public class FindRoomNextMonthAvailability {

  private final ReadModelSupplier<RoomCriteria, ManyAvailableDays> readModelSupplier;

  public FindRoomNextMonthAvailability(
      ReadModelSupplier<RoomCriteria, ManyAvailableDays> readModelSupplier) {
    this.readModelSupplier = readModelSupplier;
  }

  public ManyAvailableDays execute(FindRoomNextMonthAvailabilityQuery query) {
    final var criteria = new RoomCriteria(query.roomId());
    return readModelSupplier.get(criteria);
  }
}
