package org.egualpam.contexts.hotelmanagement.room.infrastructure.controller;

import java.util.List;

public record FindRoomsResponse(List<Room> results) {
  public record Room(String id) {}
}
