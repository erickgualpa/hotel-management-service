package org.egualpam.contexts.hotel.customer.hotel.domain;

import static java.util.Objects.isNull;

import org.egualpam.contexts.hotel.shared.domain.Criteria;
import org.egualpam.contexts.hotel.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;

public final class UniqueHotelCriteria implements Criteria {

  private final UniqueId hotelId;

  public UniqueHotelCriteria(String hotelId) {
    if (isNull(hotelId)) {
      throw new RequiredPropertyIsMissing();
    }
    this.hotelId = new UniqueId(hotelId);
  }

  public UniqueId hotelId() {
    return hotelId;
  }
}
