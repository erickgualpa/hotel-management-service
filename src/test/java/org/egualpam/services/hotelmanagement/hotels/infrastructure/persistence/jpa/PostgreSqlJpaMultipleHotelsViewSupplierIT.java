package org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotelmanagement.hotels.application.MultipleHotelsView;
import org.egualpam.services.hotelmanagement.hotels.domain.HotelCriteria;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.Criteria;
import org.egualpam.services.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceHotel;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PersistenceReview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.shuffle;
import static java.util.UUID.randomUUID;
import static java.util.stream.LongStream.range;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Transactional
@AutoConfigureTestEntityManager
class PostgreSqlJpaMultipleHotelsViewSupplierIT extends AbstractIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        // TODO: Remove this once sample hotels is no longer inserted in the database
        String clearTables = """
                DELETE FROM reviews;
                DELETE FROM hotels;
                """;
        entityManager.createNativeQuery(clearTables).executeUpdate();
    }

    @Test
    void returnHotelsSortedByAverageRating() {
        final ViewSupplier<MultipleHotelsView> testee = new PostgreSqlJpaMultipleHotelsViewSupplier(entityManager);

        UUID lowRatingHotel = randomUUID();
        UUID intermediateRatingHotel = randomUUID();
        UUID highRatingHotel = randomUUID();

        List<UUID> hotelIds = new ArrayList<>(
                Set.of(
                        lowRatingHotel,
                        intermediateRatingHotel,
                        highRatingHotel
                )
        );

        shuffle(hotelIds);

        hotelIds.forEach(this::stubHotel);

        List<Integer> lowRatingRange = List.of(1, 3);
        List<Integer> intermediateRatingRange = List.of(2, 4);
        List<Integer> highRatingRange = List.of(3, 5);

        stubReviewsWithRatingRange(lowRatingHotel, lowRatingRange);
        stubReviewsWithRatingRange(intermediateRatingHotel, intermediateRatingRange);
        stubReviewsWithRatingRange(highRatingHotel, highRatingRange);

        Criteria criteria = new HotelCriteria(
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        MultipleHotelsView result = testee.get(criteria);

        assertNotNull(result);
        assertThat(result.hotels())
                .extracting("identifier")
                .containsExactly(
                        highRatingHotel.toString(),
                        intermediateRatingHotel.toString(),
                        lowRatingHotel.toString()
                );
    }

    private void stubHotel(UUID hotelId) {
        PersistenceHotel persistenceHotel = new PersistenceHotel(
                hotelId,
                randomAlphabetic(5),
                randomAlphabetic(5),
                randomAlphabetic(5),
                nextInt(100, 200),
                randomAlphabetic(5)
        );
        testEntityManager.persistAndFlush(persistenceHotel);
    }

    private void stubReviewsWithRatingRange(UUID hotelId, List<Integer> ratingRange) {
        range(0, 20).forEach(
                i -> testEntityManager.persistAndFlush(
                        new PersistenceReview(
                                randomUUID(),
                                nextInt(
                                        ratingRange.get(0),
                                        ratingRange.get(1)
                                ),
                                randomAlphabetic(5),
                                hotelId
                        )
                )
        );
    }
}