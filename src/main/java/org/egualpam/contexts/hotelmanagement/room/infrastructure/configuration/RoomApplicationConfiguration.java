package org.egualpam.contexts.hotelmanagement.room.infrastructure.configuration;

import org.egualpam.contexts.hotelmanagement.room.application.command.CreateRoom;
import org.egualpam.contexts.hotelmanagement.room.domain.Room;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoomApplicationConfiguration {

  @Bean
  public CreateRoom createRoom(AggregateRepository<Room> repository) {
    return new CreateRoom(repository);
  }
}
