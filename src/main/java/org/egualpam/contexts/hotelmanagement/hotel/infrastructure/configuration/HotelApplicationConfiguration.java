package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.configuration;

import java.time.Clock;
import org.egualpam.contexts.hotelmanagement.hotel.application.command.CreateHotel;
import org.egualpam.contexts.hotelmanagement.hotel.application.command.UpdateHotelRating;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotel;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotels;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotelmanagement.hotel.domain.ReviewIsAlreadyProcessed;
import org.egualpam.contexts.hotelmanagement.hotel.domain.UniqueHotelCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueIdSupplier;
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
      UniqueIdSupplier uniqueIdSupplier,
      AggregateRepository<Hotel> hotelRepository,
      EventBus eventBus) {
    return new CreateHotel(clock, uniqueIdSupplier, hotelRepository, eventBus);
  }

  @Bean
  public UpdateHotelRating updateHotelRating(
      Clock clock,
      UniqueIdSupplier uniqueIdSupplier,
      AggregateRepository<Hotel> hotelRepository,
      ReviewIsAlreadyProcessed reviewIsAlreadyProcessed,
      EventBus eventBus) {
    return new UpdateHotelRating(
        clock, uniqueIdSupplier, hotelRepository, reviewIsAlreadyProcessed, eventBus);
  }
}
