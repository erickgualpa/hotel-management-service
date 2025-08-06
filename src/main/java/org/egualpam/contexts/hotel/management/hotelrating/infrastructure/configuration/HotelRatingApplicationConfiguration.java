package org.egualpam.contexts.hotel.management.hotelrating.infrastructure.configuration;

import java.time.Clock;
import java.util.function.Supplier;
import org.egualpam.contexts.hotel.management.hotelrating.application.command.InitializeHotelRating;
import org.egualpam.contexts.hotel.management.hotelrating.application.command.UpdateHotelRating;
import org.egualpam.contexts.hotel.management.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotel.management.hotelrating.domain.HotelRatingIdGenerator;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.domain.EventBus;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelRatingApplicationConfiguration {

  @Bean
  public InitializeHotelRating initializeHotelRating(
      Supplier<UniqueId> uniqueIdSupplier,
      Clock clock,
      HotelRatingIdGenerator hotelRatingIdGenerator,
      AggregateRepository<HotelRating> repository,
      EventBus eventBus) {
    return new InitializeHotelRating(
        uniqueIdSupplier, clock, hotelRatingIdGenerator, repository, eventBus);
  }

  @Bean("updateHotelRatingV2")
  public UpdateHotelRating updateHotelRating(
      Supplier<UniqueId> uniqueIdSupplier,
      Clock clock,
      HotelRatingIdGenerator hotelRatingIdGenerator,
      AggregateRepository<HotelRating> repository,
      EventBus eventBus) {
    return new UpdateHotelRating(
        uniqueIdSupplier, clock, hotelRatingIdGenerator, repository, eventBus);
  }
}
