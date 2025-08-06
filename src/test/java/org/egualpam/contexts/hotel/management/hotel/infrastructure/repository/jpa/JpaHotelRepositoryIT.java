package org.egualpam.contexts.hotel.management.hotel.infrastructure.repository.jpa;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.egualpam.contexts.AbstractIntegrationTest;
import org.egualpam.contexts.hotel.management.hotel.domain.Hotel;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class JpaHotelRepositoryIT extends AbstractIntegrationTest {

  @Autowired private EntityManager entityManager;

  @Test
  void returnEmptyOptional_whenNoHotelMatchesId() {
    final AggregateRepository<Hotel> testee = new JpaHotelRepository(entityManager);
    AggregateId hotelId = new AggregateId(randomUUID().toString());
    Optional<Hotel> result = testee.find(hotelId);
    assertThat(result).isEmpty();
  }
}
