package org.egualpam.contexts.hotelmanagement.room.application.query;

import org.egualpam.contexts.hotel.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.room.domain.RoomCriteria;

public class FindRooms {

  private final ReadModelSupplier<RoomCriteria, ManyRooms> readModelSupplier;

  public FindRooms(ReadModelSupplier<RoomCriteria, ManyRooms> readModelSupplier) {
    this.readModelSupplier = readModelSupplier;
  }

  public ManyRooms execute(FindRoomsQuery query) {
    final var criteria =
        RoomCriteria.byAvailabilityDateRange(query.availableFrom(), query.availableTo());
    return readModelSupplier.get(criteria);
  }
}
