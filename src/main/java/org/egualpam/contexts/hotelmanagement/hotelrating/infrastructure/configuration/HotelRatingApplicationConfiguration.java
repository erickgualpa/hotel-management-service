package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.configuration;

import java.time.Clock;
import org.egualpam.contexts.hotelmanagement.hotelrating.application.InitializeHotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueIdSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelRatingApplicationConfiguration {

  @Bean
  public InitializeHotelRating initializeHotelRating(
      UniqueIdSupplier uniqueIdSupplier, Clock clock, AggregateRepository<HotelRating> repository) {
    EventBus fakeEventBus = events -> {};
    return new InitializeHotelRating(uniqueIdSupplier, clock, repository, fakeEventBus);
  }
}
