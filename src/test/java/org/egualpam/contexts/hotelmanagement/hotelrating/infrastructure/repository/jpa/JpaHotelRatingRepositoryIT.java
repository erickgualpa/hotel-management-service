package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.repository.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

class JpaHotelRatingRepositoryIT extends AbstractIntegrationTest {

  @Autowired private ObjectMapper objectMapper;
  @Autowired private EntityManager entityManager;
  @Autowired private TransactionTemplate transactionTemplate;
  @Autowired private NamedParameterJdbcTemplate jdbcTemplate;

  private AggregateRepository<HotelRating> testee;

  @BeforeEach
  void setUp() {
    testee = new JpaHotelRatingRepository(objectMapper, entityManager, jdbcTemplate);
  }

  @Test
  void returnEmptyOptional_whenNoReviewMatchesId() {
    UniqueId id = UniqueId.get();
    Optional<HotelRating> result = testee.find(new AggregateId(id.value()));
    assertThat(result).isEmpty();
  }

  @Test
  void returnHotelRating_whenHotelRatingIsAlreadyInitialized() {
    UniqueId id = UniqueId.get();
    UniqueId hotelId = UniqueId.get();
    AggregateId aggregateId = new AggregateId(id.value());

    HotelRating hotelRating = HotelRating.load(id.value(), hotelId.value(), Set.of(), 0.0);

    transactionTemplate.executeWithoutResult(ts -> testee.save(hotelRating));

    Optional<HotelRating> result = testee.find(aggregateId);

    assertThat(result).isPresent();
    assertThat(result.get()).usingRecursiveComparison().isEqualTo(hotelRating);
  }
}
