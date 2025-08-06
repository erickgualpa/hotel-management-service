package org.egualpam.contexts.hotel.management.roomprice.infrastructure.configuration;

import org.egualpam.contexts.hotel.management.roomprice.application.command.UpdateRoomPrice;
import org.egualpam.contexts.hotel.management.roomprice.domain.RoomPrice;
import org.egualpam.contexts.hotel.management.roomprice.domain.RoomPriceIdGenerator;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoomPriceApplicationConfiguration {

  @Bean
  public UpdateRoomPrice updateRoomPrice(
      RoomPriceIdGenerator roomPriceIdGenerator, AggregateRepository<RoomPrice> repository) {
    return new UpdateRoomPrice(roomPriceIdGenerator, repository);
  }
}
