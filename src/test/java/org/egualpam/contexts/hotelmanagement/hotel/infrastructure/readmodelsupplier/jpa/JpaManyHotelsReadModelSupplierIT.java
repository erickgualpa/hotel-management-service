package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.readmodelsupplier.jpa;

import static java.util.Collections.shuffle;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.HotelTestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class JpaManyHotelsReadModelSupplierIT extends AbstractIntegrationTest {

  @Autowired private HotelTestRepository hotelTestRepository;
  @Autowired private EntityManager entityManager;

  private ReadModelSupplier<HotelCriteria, ManyHotels> testee;

  @BeforeEach
  void setUp() {
    testee = new JpaManyHotelsReadModelSupplier(entityManager);
  }

  @Test
  void returnHotelsSortedByAverageRating() {
    UUID lowRatingHotel = randomUUID();
    UUID intermediateRatingHotel = randomUUID();
    UUID highRatingHotel = randomUUID();

    String hotelLocation = randomAlphabetic(5);

    List<UUID> hotelIds =
        new ArrayList<>(Set.of(lowRatingHotel, intermediateRatingHotel, highRatingHotel));

    shuffle(hotelIds);

    hotelIds.forEach(hotelId -> stubHotel(hotelId, hotelLocation));

    hotelTestRepository.insertHotelAverageRating(lowRatingHotel, 1.0);
    hotelTestRepository.insertHotelAverageRating(intermediateRatingHotel, 3.0);
    hotelTestRepository.insertHotelAverageRating(highRatingHotel, 5.0);

    HotelCriteria criteria = new HotelCriteria(hotelLocation, null, null);

    ManyHotels result = testee.get(criteria);

    assertNotNull(result);
    assertThat(result.hotels())
        .extracting("identifier")
        .containsExactly(
            highRatingHotel.toString(),
            intermediateRatingHotel.toString(),
            lowRatingHotel.toString());
  }

  private void stubHotel(UUID hotelId, String hotelLocation) {
    hotelTestRepository.insertHotel(
        hotelId,
        randomAlphabetic(5),
        randomAlphabetic(5),
        hotelLocation,
        nextInt(100, 200),
        randomAlphabetic(5));
  }
}
