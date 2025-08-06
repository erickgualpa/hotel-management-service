package org.egualpam.contexts.hotel.customer.hotel.application.query;

import org.egualpam.contexts.hotel.customer.hotel.domain.UniqueHotelCriteria;
import org.egualpam.contexts.hotel.shared.application.query.ReadModelSupplier;

public class FindHotel {

  private final ReadModelSupplier<UniqueHotelCriteria, OneHotel> readModelSupplier;

  public FindHotel(ReadModelSupplier<UniqueHotelCriteria, OneHotel> readModelSupplier) {
    this.readModelSupplier = readModelSupplier;
  }

  public OneHotel execute(FindHotelQuery query) {
    String hotelId = query.hotelId();
    UniqueHotelCriteria criteria = new UniqueHotelCriteria(hotelId);
    return readModelSupplier.get(criteria);
  }
}
