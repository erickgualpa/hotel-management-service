package org.egualpam.contexts.hotelmanagement.room.application.query;

import org.egualpam.contexts.hotelmanagement.room.domain.RoomCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;

public class FindRooms {

  private final ReadModelSupplier<RoomCriteria, ManyRooms> readModelSupplier;

  public FindRooms(ReadModelSupplier<RoomCriteria, ManyRooms> readModelSupplier) {
    this.readModelSupplier = readModelSupplier;
  }

  public ManyRooms execute(FindRoomsQuery query) {
    final var criteria = new RoomCriteria(query.availableFrom(), query.availableTo());
    return readModelSupplier.get(criteria);
  }
}
