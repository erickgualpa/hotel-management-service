package org.egualpam.contexts.hotelmanagement.review.domain;

import java.util.Objects;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;

// TODO: Check if makes more sense to use EntityId or specific ids
public final class HotelId {

  private final UniqueId value;

  public HotelId(String value) {
    this.value = new UniqueId(value);
  }

  public String value() {
    return value.value();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HotelId hotelId = (HotelId) o;
    return Objects.equals(value, hotelId.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
