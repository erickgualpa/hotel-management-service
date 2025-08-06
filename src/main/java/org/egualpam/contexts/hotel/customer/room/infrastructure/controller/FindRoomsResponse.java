package org.egualpam.contexts.hotel.customer.room.infrastructure.controller;

import java.util.List;

public record FindRoomsResponse(List<Room> results) {
  public record Room(String id) {}
}
