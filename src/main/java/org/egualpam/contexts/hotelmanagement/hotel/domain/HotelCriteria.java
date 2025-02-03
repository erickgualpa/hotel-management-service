package org.egualpam.contexts.hotelmanagement.hotel.domain;

import static java.util.Objects.nonNull;

import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;

public final class HotelCriteria implements Criteria {

  private final Location location;
  private final Price minPrice;
  private final Price maxPrice;

  public HotelCriteria(String location, Integer minPrice, Integer maxPrice) {
    this.location = nonNull(location) ? new Location(location) : null;

    if (nonNull(minPrice) && nonNull(maxPrice) && minPrice > maxPrice) {
      throw new PriceRangeValuesSwapped();
    }
    this.minPrice = nonNull(minPrice) ? new Price(minPrice) : null;
    this.maxPrice = nonNull(maxPrice) ? new Price(maxPrice) : null;
  }

  public Optional<String> getLocation() {
    return Optional.ofNullable(location).map(Location::value);
  }

  public Optional<Integer> getMinPrice() {
    return Optional.ofNullable(minPrice).map(Price::value);
  }

  public Optional<Integer> getMaxPrice() {
    return Optional.ofNullable(maxPrice).map(Price::value);
  }
}
