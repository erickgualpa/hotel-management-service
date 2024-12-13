package org.egualpam.contexts.hotelmanagement.hotel.application.command;

import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;

public record UpdateHotelRatingCommand(String hotelId, Integer rating) {
  @Override
  public String hotelId() {
    return Optional.ofNullable(hotelId).orElseThrow(RequiredPropertyIsMissing::new);
  }

  @Override
  public Integer rating() {
    return Optional.ofNullable(rating).orElseThrow(RequiredPropertyIsMissing::new);
  }
}
