package org.egualpam.contexts.hotel.customer.hotel.infrastructure.configuration;

import org.egualpam.contexts.hotel.customer.hotel.application.query.FindHotel;
import org.egualpam.contexts.hotel.customer.hotel.application.query.FindHotels;
import org.egualpam.contexts.hotel.customer.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotel.customer.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotel.customer.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotel.customer.hotel.domain.UniqueHotelCriteria;
import org.egualpam.contexts.hotel.shared.application.query.ReadModelSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("hotelCustomerApplicationConfiguration")
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
