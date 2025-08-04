package org.egualpam.contexts.hotelmanagement.roomprice.application.command;

import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPrice;
import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPriceIdGenerator;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;

public class UpdateRoomPrice {

  private final RoomPriceIdGenerator roomPriceIdGenerator;
  private final AggregateRepository<RoomPrice> repository;

  public UpdateRoomPrice(
      RoomPriceIdGenerator roomPriceIdGenerator, AggregateRepository<RoomPrice> repository) {
    this.roomPriceIdGenerator = roomPriceIdGenerator;
    this.repository = repository;
  }

  public void execute(UpdateRoomPriceCommand command) {
    final var roomPrice =
        RoomPrice.create(
            roomPriceIdGenerator, command.hotelId(), command.roomType(), command.priceAmount());
    repository.save(roomPrice);
  }
}
