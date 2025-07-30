package org.egualpam.contexts.hotelmanagement.room.infrastructure.configuration;

import org.egualpam.contexts.hotelmanagement.room.application.command.CreateRoom;
import org.egualpam.contexts.hotelmanagement.room.application.query.FindRoomNextMonthAvailability;
import org.egualpam.contexts.hotelmanagement.room.application.query.ManyAvailableDays;
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
  public FindRoomNextMonthAvailability findRoomNextMonthAvailability(
      ReadModelSupplier<RoomCriteria, ManyAvailableDays> readModelSupplier) {
    return new FindRoomNextMonthAvailability(readModelSupplier);
  }
}
