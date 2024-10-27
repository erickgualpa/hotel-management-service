package org.egualpam.contexts.hotelmanagement.hotel.application.query;

import java.util.List;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModel;

public record ManyHotels(List<Hotel> hotels) implements ReadModel {
  public record Hotel(
      String identifier,
      String name,
      String description,
      String location,
      Integer price,
      String imageURL,
      Double averageRating) {}
}
