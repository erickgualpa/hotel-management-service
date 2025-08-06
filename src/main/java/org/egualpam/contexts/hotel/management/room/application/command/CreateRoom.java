package org.egualpam.contexts.hotel.management.room.application.command;

import org.egualpam.contexts.hotel.management.room.domain.Room;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;

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
