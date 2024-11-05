package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.configuration;

import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotel;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotels;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotelmanagement.hotel.domain.UniqueHotelCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
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
}
