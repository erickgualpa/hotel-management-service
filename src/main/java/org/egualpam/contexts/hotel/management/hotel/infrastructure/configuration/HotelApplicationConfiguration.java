package org.egualpam.contexts.hotel.management.hotel.infrastructure.configuration;

import java.time.Clock;
import java.util.function.Supplier;
import org.egualpam.contexts.hotel.management.hotel.application.command.CreateHotel;
import org.egualpam.contexts.hotel.management.hotel.domain.Hotel;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.domain.EventBus;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("managementHotelApplicationConfiguration")
public class HotelApplicationConfiguration {

  @Bean
  public CreateHotel createHotel(
      Clock clock,
      Supplier<UniqueId> uniqueIdSupplier,
      AggregateRepository<Hotel> hotelRepository,
      EventBus eventBus) {
    return new CreateHotel(clock, uniqueIdSupplier, hotelRepository, eventBus);
  }
}
