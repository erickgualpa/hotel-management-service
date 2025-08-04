package org.egualpam.contexts.hotelmanagement.roomprice.application.command;

import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPrice;
import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPriceIdGenerator;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.RoomType;

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

    final var roomPriceId =
        roomPriceIdGenerator.get(new AggregateId(hotelId), RoomType.valueOf(roomType));

    final var roomPrice =
        repository
            .find(new AggregateId(roomPriceId))
            .orElseGet(
                () ->
                    RoomPrice.create(
                        roomPriceIdGenerator, repository, hotelId, roomType, priceAmount));

    roomPrice.updatePriceAmount(priceAmount);

    repository.save(roomPrice);
  }
}
