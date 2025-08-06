package org.egualpam.contexts.hotel.management.room.infrastructure.configuration;

import org.egualpam.contexts.hotel.management.room.application.command.CreateRoom;
import org.egualpam.contexts.hotel.management.room.domain.Room;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("managementRoomApplicationConfiguration")
public class RoomApplicationConfiguration {

  @Bean
  public CreateRoom createRoom(AggregateRepository<Room> repository) {
    return new CreateRoom(repository);
  }
}
