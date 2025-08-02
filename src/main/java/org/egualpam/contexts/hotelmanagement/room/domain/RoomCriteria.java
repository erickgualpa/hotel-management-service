package org.egualpam.contexts.hotelmanagement.room.domain;

import static java.util.Objects.isNull;

import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;
import org.egualpam.contexts.hotelmanagement.shared.domain.DateRange;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;

public class RoomCriteria implements Criteria {

  private final DateRange availabilityDateRange;

  private RoomCriteria(String availableFrom, String availableTo) {
    this.availabilityDateRange =
        (isNull(availableFrom) || isNull(availableTo))
            ? null
            : new DateRange(availableFrom, availableTo);
  }

  public static RoomCriteria byAvailabilityDateRange(String availableFrom, String availableTo) {
    if (isNull(availableFrom) || isNull(availableTo)) {
      throw new RequiredPropertyIsMissing();
    }
    return new RoomCriteria(availableFrom, availableTo);
  }

  public String getAvailableFrom() {
    return this.availabilityDateRange.from();
  }

  public String getAvailableTo() {
    return this.availabilityDateRange.to();
  }
}
