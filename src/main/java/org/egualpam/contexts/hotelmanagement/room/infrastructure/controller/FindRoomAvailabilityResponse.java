package org.egualpam.contexts.hotelmanagement.room.infrastructure.controller;

import java.util.List;

public record FindRoomAvailabilityResponse(List<AvailableDay> results) {
  public record AvailableDay(int day, int month, int year) {}
}
