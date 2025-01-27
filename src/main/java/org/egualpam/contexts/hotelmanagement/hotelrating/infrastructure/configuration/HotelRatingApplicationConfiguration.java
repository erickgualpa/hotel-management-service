package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.configuration;

import org.egualpam.contexts.hotelmanagement.hotelrating.application.InitializeHotelRating;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelRatingApplicationConfiguration {

  @Bean
  public InitializeHotelRating initializeHotelRating() {
    return new InitializeHotelRating();
  }
}
