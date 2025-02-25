package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.configuration;

import java.time.Clock;
import java.util.function.Supplier;
import org.egualpam.contexts.hotelmanagement.hotel.application.command.CreateHotel;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotel;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotels;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotelmanagement.hotel.domain.UniqueHotelCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelApplicationConfiguration {

  @Bean
  public FindHotel findHotel(
      ReadModelSupplier<UniqueHotelCriteria, OneHotel> oneHotelReadModelSupplier) {
    return new FindHotel(oneHotelReadModelSupplier);
  }

  @Bean
  public FindHotels findHotels(
      ReadModelSupplier<HotelCriteria, ManyHotels> manyHotelsReadModelSupplier) {
    return new FindHotels(manyHotelsReadModelSupplier);
  }

  @Bean
  public CreateHotel createHotel(
      Clock clock,
      Supplier<UniqueId> uniqueIdSupplier,
      AggregateRepository<Hotel> hotelRepository,
      EventBus eventBus) {
    return new CreateHotel(clock, uniqueIdSupplier, hotelRepository, eventBus);
  }
}
