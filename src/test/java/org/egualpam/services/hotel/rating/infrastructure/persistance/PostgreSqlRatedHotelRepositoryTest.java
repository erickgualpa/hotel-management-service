package org.egualpam.services.hotel.rating.infrastructure.persistance;

import org.egualpam.services.hotel.rating.AbstractIntegrationTest;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostgreSqlRatedHotelRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private RatedHotelRepository testee;

    @Test
    void givenAnyQuery_matchingRatedHotelsShouldBeReturned() {

        // TODO: Complete this test case (Query filtering is not implemented yet!)

        List<RatedHotel> result = testee.findHotelsMatchingQuery(HotelQuery.create().build());

        assertThat(result).hasSize(1)
                .allSatisfy(
                        actualHotel ->
                        {
                            assertThat(actualHotel.getIdentifier()).isEqualTo("1");
                            assertThat(actualHotel.getName()).isEqualTo("Amazing hotel");
                            assertThat(actualHotel.getDescription()).isEqualTo("Eloquent description");
                            assertThat(actualHotel.getTotalPrice()).isEqualTo(150);
                            assertThat(actualHotel.getImageURL()).isEqualTo("amazing-hotel-image.com");

                            assertThat(actualHotel.getLocation()).satisfies(
                                    actualHotelLocation -> {
                                        assertNotNull(actualHotelLocation.getIdentifier());
                                        assertThat(actualHotelLocation.getName()).isEqualTo("Barcelona");
                                    });
                        }

                );
    }
}
