package org.egualpam.contexts.hotelmanagement.hotel.application.query;

import org.egualpam.contexts.hotelmanagement.hotel.domain.UniqueHotelCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;

public class FindHotel {

  private final ReadModelSupplier<OneHotel> readModelSupplier;

  public FindHotel(ReadModelSupplier<OneHotel> readModelSupplier) {
    this.readModelSupplier = readModelSupplier;
  }

  public OneHotel execute(FindHotelQuery query) {
    String hotelId = query.hotelId();
    Criteria criteria = new UniqueHotelCriteria(hotelId);
    return readModelSupplier.get(criteria);
  }
}
