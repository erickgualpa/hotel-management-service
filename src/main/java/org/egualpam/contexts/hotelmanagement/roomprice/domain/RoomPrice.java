package org.egualpam.contexts.hotelmanagement.roomprice.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotelmanagement.shared.domain.RoomType;

public class RoomPrice extends AggregateRoot {

  private final AggregateId hotelId;
  private final RoomType roomType;
  private final Price price;

  private RoomPrice(
      RoomPriceIdGenerator roomPriceIdGenerator, String hotelId, String roomType, String amount) {
    super(generateId(roomPriceIdGenerator, hotelId, roomType));
    this.hotelId = new AggregateId(hotelId);
    this.roomType = RoomType.valueOf(roomType);
    this.price = Price.of(amount);
  }

  private static String generateId(
      RoomPriceIdGenerator roomPriceIdGenerator, String hotelId, String roomType) {
    return roomPriceIdGenerator.get(new AggregateId(hotelId), RoomType.valueOf(roomType));
  }

  public static RoomPrice create(
      RoomPriceIdGenerator roomPriceIdGenerator,
      String hotelId,
      String roomType,
      String priceAmount) {
    return new RoomPrice(roomPriceIdGenerator, hotelId, roomType, priceAmount);
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
