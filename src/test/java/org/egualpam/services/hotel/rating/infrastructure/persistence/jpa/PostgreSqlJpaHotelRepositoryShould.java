package org.egualpam.services.hotel.rating.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.domain.hotels.AverageRating;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelName;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.hotels.InvalidPriceRange;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Double.parseDouble;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestConfiguration
class PostgreSqlJpaHotelRepositoryTestConfiguration {

    @Bean
    public HotelRepository testee(EntityManager entityManager) {
        return new PostgreSqlJpaHotelRepository(entityManager);
    }
}

@Transactional
@AutoConfigureTestEntityManager
@Import(PostgreSqlJpaHotelRepositoryTestConfiguration.class)
class PostgreSqlJpaHotelRepositoryShould extends AbstractIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private HotelRepository testee;

    @Test
    void throwInvalidPriceRange_whenMinPriceFilterIsGreaterThanMaxPriceFilter() {
        Optional<Location> locationFilter = Optional.empty();
        Optional<Price> minPriceFilter = Optional.of(new Price(100));
        Optional<Price> maxPriceFilter = Optional.of(new Price(50));
        assertThrows(
                InvalidPriceRange.class,
                () -> testee.find(
                        locationFilter,
                        minPriceFilter,
                        maxPriceFilter
                )
        );
    }
}