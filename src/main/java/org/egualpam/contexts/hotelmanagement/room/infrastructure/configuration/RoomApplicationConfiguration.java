package org.egualpam.contexts.hotelmanagement.room.infrastructure.configuration;

import org.egualpam.contexts.hotelmanagement.room.application.command.CreateRoom;
import org.egualpam.contexts.hotelmanagement.room.application.query.FindRooms;
import org.egualpam.contexts.hotelmanagement.room.application.query.ManyRooms;
import org.egualpam.contexts.hotelmanagement.room.domain.Room;
import org.egualpam.contexts.hotelmanagement.room.domain.RoomCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoomApplicationConfiguration {

  @Bean
  public CreateRoom createRoom(AggregateRepository<Room> repository) {
    return new CreateRoom(repository);
  }

  @Bean
  public FindRooms findRoomNextMonthAvailability(
      ReadModelSupplier<RoomCriteria, ManyRooms> readModelSupplier) {
    return new FindRooms(readModelSupplier);
  }
}
