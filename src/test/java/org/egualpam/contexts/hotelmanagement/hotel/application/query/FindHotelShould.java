package org.egualpam.contexts.hotelmanagement.hotel.application.query;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindHotelShould {

  @Mock private ReadModelSupplier<OneHotel> readModelSupplier;

  private FindHotel testee;

  @BeforeEach
  void setUp() {
    testee = new FindHotel(readModelSupplier);
  }

  @Test
  void findHotel() {
    String hotelId = randomUUID().toString();
    String name = randomAlphabetic(5);
    String description = randomAlphabetic(5);
    String location = randomAlphabetic(5);
    Integer price = 50;
    String imageURL = "www." + randomAlphabetic(5) + ".com";
    Double averageRating = 2.0;

    OneHotel.Hotel hotel =
        new OneHotel.Hotel(hotelId, name, description, location, price, imageURL, averageRating);
    when(readModelSupplier.get(any(Criteria.class))).thenReturn(new OneHotel(Optional.of(hotel)));

    FindHotelQuery query = new FindHotelQuery(hotelId);

    OneHotel result = testee.execute(query);

    OneHotel.Hotel expected =
        new OneHotel.Hotel(hotelId, name, description, location, price, imageURL, averageRating);
    assertThat(result.hotel()).isPresent().get().isEqualTo(expected);
  }

  @Test
  void throwDomainException_whenHotelIdIsMissing() {
    FindHotelQuery criteria = new FindHotelQuery(null);
    assertThrows(RequiredPropertyIsMissing.class, () -> testee.execute(criteria));
  }

  @Test
  void findEmptyWhenNoHotelFound() {
    String hotelId = randomUUID().toString();
    when(readModelSupplier.get(any(Criteria.class))).thenReturn(new OneHotel(Optional.empty()));
    FindHotelQuery query = new FindHotelQuery(hotelId);

    OneHotel result = testee.execute(query);

    assertThat(result.hotel()).isEmpty();
  }
}
