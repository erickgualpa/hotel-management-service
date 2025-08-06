package org.egualpam.contexts.hotelmanagement.roomprice.domain;

import static java.util.Objects.isNull;

import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotel.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotel.shared.domain.RoomType;

public class RoomPrice extends AggregateRoot {

  private final AggregateId hotelId;
  private final RoomType roomType;
  private Price price;

  private RoomPrice(String id, String hotelId, String roomType, String amount) {
    super(id);
    if (isNull(hotelId) || isNull(roomType)) {
      throw new RequiredPropertyIsMissing();
    }
    this.hotelId = new AggregateId(hotelId);
    this.roomType = RoomType.valueOf(roomType);
    this.price = Price.of(amount);
  }

  public static AggregateId generateId(
      RoomPriceIdGenerator idGenerator, String hotelId, String roomType) {
    if (isNull(hotelId) || isNull(roomType)) {
      throw new RequiredPropertyIsMissing();
    }

    final var hotelAggregateId = new AggregateId(hotelId);
    final var roomTypeValue = RoomType.valueOf(roomType);

    return idGenerator.get(hotelAggregateId, roomTypeValue);
  }

  public static RoomPrice load(
      String roomPriceId, String hotelId, String roomType, String priceAmount) {
    return new RoomPrice(roomPriceId, hotelId, roomType, priceAmount);
  }

  public static RoomPrice create(
      String roomPriceId, String hotelId, String roomType, String priceAmount) {
    return new RoomPrice(roomPriceId, hotelId, roomType, priceAmount);
  }

  public void updatePriceAmount(String newPriceAmount) {
    if (isNull(newPriceAmount)) {
      throw new RequiredPropertyIsMissing();
    }
    this.price = this.price.updateAmount(newPriceAmount);
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
