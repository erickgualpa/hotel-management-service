package org.egualpam.contexts.hotel.customer.room.infrastructure.configuration;

import org.egualpam.contexts.hotel.customer.room.application.query.FindRooms;
import org.egualpam.contexts.hotel.customer.room.application.query.ManyRooms;
import org.egualpam.contexts.hotel.customer.room.domain.RoomCriteria;
import org.egualpam.contexts.hotel.shared.application.query.ReadModelSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("customerRoomApplicationConfiguration")
public class RoomApplicationConfiguration {

  @Bean
  public FindRooms findRoomNextMonthAvailability(
      ReadModelSupplier<RoomCriteria, ManyRooms> readModelSupplier) {
    return new FindRooms(readModelSupplier);
  }
}
