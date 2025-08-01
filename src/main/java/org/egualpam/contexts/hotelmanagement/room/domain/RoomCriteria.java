package org.egualpam.contexts.hotelmanagement.room.domain;

import static java.util.Objects.isNull;

import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;

public class RoomCriteria implements Criteria {

  private final DateRange availabilityDateRange;

  public RoomCriteria(String availableFrom, String availableTo) {
    if (isNull(availableFrom) || isNull(availableTo)) {
      throw new RequiredPropertyIsMissing();
    }
    this.availabilityDateRange = new DateRange(availableFrom, availableTo);
  }
}
