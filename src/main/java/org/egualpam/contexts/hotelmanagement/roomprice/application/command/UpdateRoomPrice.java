package org.egualpam.contexts.hotelmanagement.roomprice.application.command;

import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPrice;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;

public class UpdateRoomPrice {

  private final AggregateRepository<RoomPrice> repository;

  public UpdateRoomPrice(AggregateRepository<RoomPrice> repository) {
    this.repository = repository;
  }

  public void execute(UpdateRoomPriceCommand command) {
    final var roomPrice = RoomPrice.create(command.roomPriceId());
    repository.save(roomPrice);
  }
}
