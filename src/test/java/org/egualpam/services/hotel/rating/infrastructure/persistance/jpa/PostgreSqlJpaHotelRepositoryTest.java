package org.egualpam.services.hotel.rating.infrastructure.persistance.jpa;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.egualpam.services.hotel.rating.helpers.HotelTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PostgreSqlJpaHotelRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private HotelTestRepository hotelTestRepository;

    @Autowired
    private HotelRepository testee;

    @Test
    void givenEmptyQuery_allHotelsShouldBeReturned() {
        hotelTestRepository.insertHotelWithIdentifier(UUID.randomUUID());
        List<Hotel> result = testee.findHotelsMatchingQuery(HotelQuery.create().build());
        assertThat(result).hasAtLeastOneElementOfType(Hotel.class);
    }

    @Test
    void givenQueryWithLocationFilter_matchingHotelsShouldBeReturned() {

        UUID hotelIdentifier = UUID.randomUUID();

        hotelTestRepository.insertHotelWithIdentifierAndLocation(UUID.randomUUID(), UUID.randomUUID().toString());
        hotelTestRepository.insertHotelWithIdentifierAndLocation(hotelIdentifier, "Sydney");

        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withLocation("Sydney")
                        .build();

        List<Hotel> result = testee.findHotelsMatchingQuery(hotelQuery);

        assertThat(result).hasSize(1)
                .extracting("identifier")
                .allSatisfy(
                        actualIdentifier -> assertThat(actualIdentifier).isEqualTo(hotelIdentifier.toString())
                );
    }

    @Test
    void givenQueryWithPriceRangeFilter_matchingHotelsShouldBeReturned() {

        hotelTestRepository.insertHotelWithIdentifierAndTotalPrice(UUID.randomUUID(), 50);
        hotelTestRepository.insertHotelWithIdentifierAndTotalPrice(UUID.randomUUID(), 100);
        hotelTestRepository.insertHotelWithIdentifierAndTotalPrice(UUID.randomUUID(), 125);
        hotelTestRepository.insertHotelWithIdentifierAndTotalPrice(UUID.randomUUID(), 150);
        hotelTestRepository.insertHotelWithIdentifierAndTotalPrice(UUID.randomUUID(), 500);

        List<Hotel> result = testee.findHotelsMatchingQuery(HotelQuery.create()
                .withPriceRange(100, 150)
                .build());

        assertThat(result).hasSize(3);
    }
}