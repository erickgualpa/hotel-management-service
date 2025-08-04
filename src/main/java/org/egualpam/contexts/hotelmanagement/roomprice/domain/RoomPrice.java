package org.egualpam.contexts.hotelmanagement.roomprice.domain;

import static java.util.Objects.isNull;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
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

  public static RoomPrice load(
      String roomPriceId, String hotelId, String roomType, String priceAmount) {
    return new RoomPrice(roomPriceId, hotelId, roomType, priceAmount);
  }

  public static RoomPrice create(
      RoomPriceIdGenerator roomPriceIdGenerator,
      AggregateRepository<RoomPrice> repository,
      String hotelId,
      String roomType,
      String priceAmount) {
    final var roomPriceId = generateId(roomPriceIdGenerator, hotelId, roomType);

    if (repository.find(new AggregateId(roomPriceId)).isPresent()) {
      throw new RoomPriceAlreadyExists();
    }

    return new RoomPrice(roomPriceId, hotelId, roomType, priceAmount);
  }

  private static String generateId(
      RoomPriceIdGenerator roomPriceIdGenerator, String hotelId, String roomType) {
    if (isNull(hotelId) || isNull(roomType)) {
      throw new RequiredPropertyIsMissing();
    }
    return roomPriceIdGenerator.get(new AggregateId(hotelId), RoomType.valueOf(roomType));
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
