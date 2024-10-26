package org.egualpam.contexts.hotelmanagement.hotel.application.query;

import java.util.List;
import org.egualpam.contexts.hotelmanagement.shared.application.query.View;

public record MultipleHotelsView(List<Hotel> hotels) implements View {
  public record Hotel(
      String identifier,
      String name,
      String description,
      String location,
      Integer price,
      String imageURL,
      Double averageRating) {}
}
