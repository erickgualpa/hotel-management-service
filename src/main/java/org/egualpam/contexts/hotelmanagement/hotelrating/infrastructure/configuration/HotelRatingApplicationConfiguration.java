package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.configuration;

import java.time.Clock;
import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.hotelrating.application.command.InitializeHotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.application.command.UpdateHotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueIdSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelRatingApplicationConfiguration {

  @Bean
  public InitializeHotelRating initializeHotelRating(
      UniqueIdSupplier uniqueIdSupplier,
      Clock clock,
      AggregateRepository<HotelRating> repository,
      EventBus eventBus) {
    return new InitializeHotelRating(uniqueIdSupplier, clock, repository, eventBus);
  }

  @Bean("updateHotelRatingV2")
  public UpdateHotelRating updateHotelRating(UniqueIdSupplier uniqueIdSupplier, Clock clock) {
    AggregateRepository<HotelRating> fakeRepository =
        new AggregateRepository<>() {
          @Override
          public Optional<HotelRating> find(AggregateId id) {
            return Optional.empty();
          }

          @Override
          public void save(HotelRating aggregate) {}
        };

    EventBus fakeEventBus = events -> {};

    return new UpdateHotelRating(uniqueIdSupplier, clock, fakeRepository, fakeEventBus);
  }
}
