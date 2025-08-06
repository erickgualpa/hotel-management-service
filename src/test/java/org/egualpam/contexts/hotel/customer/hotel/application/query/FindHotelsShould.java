package org.egualpam.contexts.hotel.customer.hotel.application.query;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.egualpam.contexts.hotel.customer.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotel.customer.hotel.domain.PriceRangeValuesSwapped;
import org.egualpam.contexts.hotel.shared.application.query.ReadModelSupplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindHotelsShould {

  @Mock private ReadModelSupplier<HotelCriteria, ManyHotels> readModelSupplier;

  private FindHotels testee;

  @BeforeEach
  void setUp() {
    testee = new FindHotels(readModelSupplier);
  }

  @Test
  void findHotels() {
    String identifier = randomUUID().toString();
    String name = randomAlphabetic(5);
    String description = randomAlphabetic(5);
    String location = randomAlphabetic(5);
    Integer price = 50;
    String imageURL = "www." + randomAlphabetic(5) + ".com";
    Double averageRating = 2.0;

    Integer minPrice = 50;
    Integer maxPrice = 100;

    ManyHotels.Hotel hotel =
        new ManyHotels.Hotel(
            identifier, name, description, location, price, imageURL, averageRating);
    ManyHotels manyHotels = new ManyHotels(List.of(hotel));
    when(readModelSupplier.get(any(HotelCriteria.class))).thenReturn(manyHotels);

    FindHotelsQuery query = new FindHotelsQuery(location, minPrice, maxPrice);

    ManyHotels result = testee.execute(query);

    ManyHotels.Hotel expected =
        new ManyHotels.Hotel(
            identifier, name, description, location, price, imageURL, averageRating);
    assertThat(result.hotels()).hasSize(1).first().isEqualTo(expected);
  }

  @Test
  void findEmptyWhenNoHotelsFound() {
    String location = randomAlphabetic(5);
    Integer minPrice = 50;
    Integer maxPrice = 100;

    ManyHotels manyHotels = new ManyHotels(List.of());
    when(readModelSupplier.get(any(HotelCriteria.class))).thenReturn(manyHotels);

    FindHotelsQuery query = new FindHotelsQuery(location, minPrice, maxPrice);

    ManyHotels result = testee.execute(query);

    assertThat(result.hotels()).isEmpty();
  }

  @Test
  void throwDomainExceptionWhenHotelCriteriaHasPriceRangeValuesSwapped() {
    String location = null;
    Integer minPrice = 100;
    Integer maxPrice = 50;

    FindHotelsQuery query = new FindHotelsQuery(location, minPrice, maxPrice);

    assertThrows(PriceRangeValuesSwapped.class, () -> testee.execute(query));
  }
}
