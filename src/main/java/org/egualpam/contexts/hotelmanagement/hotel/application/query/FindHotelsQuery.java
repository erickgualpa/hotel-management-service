package org.egualpam.contexts.hotelmanagement.hotel.application.query;

import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;

public final class FindHotelsQuery implements Query {
  private final String location;
  private final Integer minPrice;
  private final Integer maxPrice;

  public FindHotelsQuery(String location, Integer minPrice, Integer maxPrice) {
    this.location = location;
    this.minPrice = minPrice;
    this.maxPrice = maxPrice;
  }

  public Optional<String> getLocation() {
    return Optional.ofNullable(location);
  }

  public Optional<Integer> getMinPrice() {
    return Optional.ofNullable(minPrice);
  }

  public Optional<Integer> getMaxPrice() {
    return Optional.ofNullable(maxPrice);
  }
}
