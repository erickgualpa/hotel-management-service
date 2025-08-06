package org.egualpam.contexts.hotelmanagement.room.application.command;

import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.room.domain.Room;

public class CreateRoom {

  private final AggregateRepository<Room> repository;

  public CreateRoom(AggregateRepository<Room> repository) {
    this.repository = repository;
  }

  public void execute(CreateRoomCommand command) {
    // TODO: Handle idempotency
    final var room = Room.create(command.id(), command.type(), command.hotelId());
    repository.save(room);
  }
}
