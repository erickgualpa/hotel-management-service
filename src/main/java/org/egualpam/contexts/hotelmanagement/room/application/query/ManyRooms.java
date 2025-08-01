package org.egualpam.contexts.hotelmanagement.room.application.query;

import java.util.List;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModel;

public record ManyRooms(List<Room> rooms) implements ReadModel {

  public record Room(String id) {}
}
