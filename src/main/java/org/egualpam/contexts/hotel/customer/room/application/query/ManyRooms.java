package org.egualpam.contexts.hotel.customer.room.application.query;

import java.util.List;
import org.egualpam.contexts.hotel.shared.application.query.ReadModel;

public record ManyRooms(List<Room> rooms) implements ReadModel {

  public record Room(String id) {}
}
