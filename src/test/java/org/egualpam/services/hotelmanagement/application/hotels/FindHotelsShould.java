package org.egualpam.services.hotelmanagement.application.hotels;

import org.egualpam.services.hotelmanagement.application.shared.InternalQuery;
import org.egualpam.services.hotelmanagement.application.shared.ViewSupplier;
import org.egualpam.services.hotelmanagement.domain.hotels.exception.PriceRangeValuesSwapped;
import org.egualpam.services.hotelmanagement.domain.shared.Criteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.shuffle;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextDouble;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindHotelsShould {

    @Mock
    private ViewSupplier<HotelsView> hotelsViewSupplier;

    private InternalQuery<HotelsView> testee;

    @BeforeEach
    void setup() {
        testee = new FindHotels(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                hotelsViewSupplier
        );
    }

    @Test
    void returnHotelsMatchingFilters() {
        String identifier = randomUUID().toString();
        String name = randomAlphabetic(5);
        String description = randomAlphabetic(10);
        String location = randomAlphabetic(5);
        Integer price = nextInt(50, 1000);
        String imageURL = "www." + randomAlphabetic(5) + ".com";
        Double averageRating = nextDouble(1, 5);

        List<HotelsView.Hotel> hotels =
                List.of(
                        new HotelsView.Hotel(
                                identifier,
                                name,
                                description,
                                location,
                                price,
                                imageURL,
                                averageRating
                        )
                );

        when(hotelsViewSupplier.get(any(Criteria.class))).thenReturn(new HotelsView(hotels));

        HotelsView result = testee.get();

        assertThat(result.hotels())
                .hasSize(1)
                .first()
                .satisfies(
                        actual -> {
                            assertThat(actual.identifier()).isEqualTo(identifier);
                            assertThat(actual.name()).isEqualTo(name);
                            assertThat(actual.description()).isEqualTo(description);
                            assertThat(actual.location()).isEqualTo(location);
                            assertThat(actual.totalPrice()).isEqualTo(price);
                            assertThat(actual.imageURL()).isEqualTo(imageURL);
                            assertThat(actual.averageRating()).isEqualTo(averageRating);
                        }
                );
    }

    @Test
    void returnHotelsSortedByAverageRating() {
        String expectedFirstIdentifier = randomUUID().toString();
        Double highestAverageRating = nextDouble(4, 5);
        String expectedLastIdentifier = randomUUID().toString();
        Double lowestAverageRating = nextDouble(1, 2);

        final List<HotelsView.Hotel> hotels = new ArrayList<>();
        hotels.add(
                new HotelsView.Hotel(
                        expectedLastIdentifier,
                        randomAlphabetic(5),
                        randomAlphabetic(10),
                        randomAlphabetic(5),
                        nextInt(50, 1000),
                        "www." + randomAlphabetic(5) + ".com",
                        lowestAverageRating
                )
        );

        hotels.add(
                new HotelsView.Hotel(
                        expectedFirstIdentifier,
                        randomAlphabetic(5),
                        randomAlphabetic(10),
                        randomAlphabetic(5),
                        nextInt(50, 1000),
                        "www." + randomAlphabetic(5) + ".com",
                        highestAverageRating
                )
        );

        shuffle(hotels);

        when(hotelsViewSupplier.get(any(Criteria.class))).thenReturn(new HotelsView(hotels));

        HotelsView result = testee.get();

        assertThat(result.hotels())
                .hasSize(2)
                .satisfies(
                        actual -> {
                            assertThat(actual).first().satisfies(
                                    first -> assertThat(first.identifier()).isEqualTo(expectedFirstIdentifier)
                            );
                            assertThat(actual).last().satisfies(
                                    last -> assertThat(last.identifier()).isEqualTo(expectedLastIdentifier)
                            );
                        }
                );
    }

    @Test
    void throwDomainException_whenValuesFromPriceRangeFilterAreSwapped() {
        int minPriceValue = 50;
        int maxPriceValue = minPriceValue - 1;

        Optional<String> location = Optional.empty();
        Optional<Integer> minPrice = Optional.of(minPriceValue);
        Optional<Integer> maxPrice = Optional.of(maxPriceValue);

        assertThrows(
                PriceRangeValuesSwapped.class,
                () -> new FindHotels(
                        location,
                        minPrice,
                        maxPrice,
                        hotelsViewSupplier
                )
        );
    }
}