package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.configuration;

import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotel;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotels;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelApplicationConfiguration {

  @Bean
  public FindHotel findHotel(ReadModelSupplier<OneHotel> oneHotelReadModelSupplier) {
    return new FindHotel(oneHotelReadModelSupplier);
  }

  @Bean
  public FindHotels findHotels(ReadModelSupplier<ManyHotels> manyHotelsReadModelSupplier) {
    return new FindHotels(manyHotelsReadModelSupplier);
  }
}
