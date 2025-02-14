package org.egualpam.contexts.hotelmanagement.review.domain;

import static java.util.Objects.nonNull;

import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;

public final class ReviewCriteria implements Criteria {

  private final HotelId hotelId;

  public ReviewCriteria(String hotelId) {
    this.hotelId = nonNull(hotelId) ? new HotelId(hotelId) : null;
  }

  public Optional<HotelId> getHotelId() {
    return Optional.ofNullable(hotelId);
  }
}
