package org.egualpam.contexts.hotelmanagement.hotel.application.query;

import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.shared.application.query.View;

public record SingleHotelView(Optional<Hotel> hotel) implements View {
  public record Hotel(
      String identifier,
      String name,
      String description,
      String location,
      Integer totalPrice,
      String imageURL,
      Double averageRating) {}
}
