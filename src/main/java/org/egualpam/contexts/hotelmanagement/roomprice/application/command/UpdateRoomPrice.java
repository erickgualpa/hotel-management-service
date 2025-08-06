package org.egualpam.contexts.hotelmanagement.roomprice.application.command;

import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPrice;
import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPriceIdGenerator;

public class UpdateRoomPrice {

  private final RoomPriceIdGenerator roomPriceIdGenerator;
  private final AggregateRepository<RoomPrice> repository;

  public UpdateRoomPrice(
      RoomPriceIdGenerator roomPriceIdGenerator, AggregateRepository<RoomPrice> repository) {
    this.roomPriceIdGenerator = roomPriceIdGenerator;
    this.repository = repository;
  }

  public void execute(UpdateRoomPriceCommand command) {
    final var hotelId = command.hotelId();
    final var roomType = command.roomType();
    final var priceAmount = command.priceAmount();

    final var roomPriceId = RoomPrice.generateId(roomPriceIdGenerator, hotelId, roomType);

    final var roomPrice =
        repository
            .find(roomPriceId)
            .orElseGet(() -> RoomPrice.create(roomPriceId.value(), hotelId, roomType, priceAmount));

    roomPrice.updatePriceAmount(priceAmount);

    repository.save(roomPrice);
  }
}
