package org.egualpam.contexts.hotel.customer.hotel.application.query;

import org.egualpam.contexts.hotel.customer.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotel.shared.application.query.ReadModelSupplier;

public class FindHotels {

  private final ReadModelSupplier<HotelCriteria, ManyHotels> readModelSupplier;

  public FindHotels(ReadModelSupplier<HotelCriteria, ManyHotels> readModelSupplier) {
    this.readModelSupplier = readModelSupplier;
  }

  public ManyHotels execute(FindHotelsQuery query) {
    String location = query.location();
    Integer minPrice = query.minPrice();
    Integer maxPrice = query.maxPrice();
    HotelCriteria criteria = new HotelCriteria(location, minPrice, maxPrice);
    return readModelSupplier.get(criteria);
  }
}
