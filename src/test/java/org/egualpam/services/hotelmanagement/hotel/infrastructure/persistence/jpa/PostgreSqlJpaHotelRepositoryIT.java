package org.egualpam.services.hotelmanagement.hotel.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

class PostgreSqlJpaHotelRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void returnEmptyOptional_whenNoHotelMatchesId() {
        final AggregateRepository<Hotel> testee = new PostgreSqlJpaHotelRepository(entityManager);
        AggregateId hotelId = new AggregateId(randomUUID().toString());
        Optional<Hotel> result = testee.find(hotelId);
        assertThat(result).isEmpty();
    }
}