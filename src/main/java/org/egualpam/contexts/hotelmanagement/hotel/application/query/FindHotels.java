package org.egualpam.contexts.hotelmanagement.hotel.application.query;

import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;

public class FindHotels {

  private final ReadModelSupplier<ManyHotels> readModelSupplier;

  public FindHotels(ReadModelSupplier<ManyHotels> readModelSupplier) {
    this.readModelSupplier = readModelSupplier;
  }

  public ManyHotels execute(FindHotelsQuery query) {
    String location = query.location();
    Integer minPrice = query.minPrice();
    Integer maxPrice = query.maxPrice();
    Criteria criteria = new HotelCriteria(location, minPrice, maxPrice);
    return readModelSupplier.get(criteria);
  }
}
