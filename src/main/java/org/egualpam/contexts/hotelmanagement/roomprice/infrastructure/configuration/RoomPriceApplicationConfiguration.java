package org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.configuration;

import org.egualpam.contexts.hotelmanagement.roomprice.application.command.UpdateRoomPrice;
import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPrice;
import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPriceIdGenerator;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
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
