package org.egualpam.contexts.hotelmanagement.hotel.domain;

import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public final class UniqueHotelCriteria implements Criteria {

  private final UniqueId hotelId;

  public UniqueHotelCriteria(String hotelId) {
    this.hotelId =
        Optional.ofNullable(hotelId).map(UniqueId::new).orElseThrow(RequiredPropertyIsMissing::new);
  }

  public UniqueId hotelId() {
    return hotelId;
  }
}
