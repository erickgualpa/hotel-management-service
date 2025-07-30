package org.egualpam.contexts.hotelmanagement.room.application.query;

import java.util.List;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModel;

public record ManyAvailableDays(List<AvailableDay> availableDays) implements ReadModel {
  public record AvailableDay(int day, int month, int year) {}
}
