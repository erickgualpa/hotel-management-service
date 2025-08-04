package org.egualpam.contexts.hotelmanagement.roomprice.domain;

import static java.util.Objects.isNull;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotelmanagement.shared.domain.RoomType;

public class RoomPrice extends AggregateRoot {

  private final AggregateId hotelId;
  private final RoomType roomType;
  private final Price price;

  private RoomPrice(String id, String hotelId, String roomType, String amount) {
    super(id);
    if (isNull(hotelId) || isNull(roomType)) {
      throw new RequiredPropertyIsMissing();
    }
    this.hotelId = new AggregateId(hotelId);
    this.roomType = RoomType.valueOf(roomType);
    this.price = Price.of(amount);
  }

  public static RoomPrice create(String id, String hotelId, String roomType, String priceAmount) {
    return new RoomPrice(id, hotelId, roomType, priceAmount);
  }

  public String hotelId() {
    return this.hotelId.value();
  }

  public String roomType() {
    return this.roomType.name();
  }

  public Price price() {
    return this.price;
  }
}
