package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.shared.jpa.hotelaveragerating;

import java.math.BigDecimal;

public record HotelAverageRating(Integer reviewsCount, BigDecimal average) {
  public Double value() {
    return this.average.doubleValue();
  }
}
