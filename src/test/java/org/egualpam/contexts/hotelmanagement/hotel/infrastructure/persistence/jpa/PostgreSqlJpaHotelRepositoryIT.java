package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.persistence.jpa;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PostgreSqlJpaHotelRepositoryIT extends AbstractIntegrationTest {

  @Autowired private EntityManager entityManager;

  @Test
  void returnEmptyOptional_whenNoHotelMatchesId() {
    final AggregateRepository<Hotel> testee = new PostgreSqlJpaHotelRepository(entityManager);
    AggregateId hotelId = new AggregateId(randomUUID().toString());
    Optional<Hotel> result = testee.find(hotelId);
    assertThat(result).isEmpty();
  }
}
