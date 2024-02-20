package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.application.shared.InternalQuery;
import org.egualpam.services.hotel.rating.domain.hotels.AverageRating;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelDescription;
import org.egualpam.services.hotel.rating.domain.hotels.HotelName;
import org.egualpam.services.hotel.rating.domain.hotels.ImageURL;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.egualpam.services.hotel.rating.domain.hotels.exception.PriceRangeValuesSwapped;
import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;
import org.egualpam.services.hotel.rating.domain.shared.Criteria;
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
    private AggregateRepository<Hotel> aggregateHotelRepository;

    private InternalQuery<List<HotelDto>> testee;

    @BeforeEach
    void setup() {
        testee = new FindHotels(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                aggregateHotelRepository
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

        List<Hotel> hotels =
                List.of(
                        new Hotel(
                                new AggregateId(identifier),
                                new HotelName(name),
                                new HotelDescription(description),
                                new Location(location),
                                new Price(price),
                                new ImageURL(imageURL),
                                new AverageRating(averageRating)
                        )
                );

        when(aggregateHotelRepository.find(any(Criteria.class))).thenReturn(hotels);

        List<HotelDto> result = testee.get();

        assertThat(result)
                .hasSize(1)
                .allSatisfy(
                        actualHotel -> {
                            assertThat(actualHotel.identifier()).isEqualTo(identifier);
                            assertThat(actualHotel.name()).isEqualTo(name);
                            assertThat(actualHotel.description()).isEqualTo(description);
                            assertThat(actualHotel.location()).isEqualTo(location);
                            assertThat(actualHotel.totalPrice()).isEqualTo(price);
                            assertThat(actualHotel.imageURL()).isEqualTo(imageURL);
                            assertThat(actualHotel.averageRating()).isEqualTo(averageRating);
                        }
                );
    }

    @Test
    void returnHotelsSortedByAverageRating() {
        String expectedFirstIdentifier = randomUUID().toString();
        Double highestAverageRating = nextDouble(4, 5);
        String expectedLastIdentifier = randomUUID().toString();
        Double lowestAverageRating = nextDouble(1, 2);

        final List<Hotel> hotels = new ArrayList<>();
        hotels.add(
                new Hotel(
                        new AggregateId(expectedLastIdentifier),
                        new HotelName(randomAlphabetic(5)),
                        new HotelDescription(randomAlphabetic(10)),
                        new Location(randomAlphabetic(5)),
                        new Price(nextInt(50, 1000)),
                        new ImageURL("www." + randomAlphabetic(5) + ".com"),
                        new AverageRating(lowestAverageRating)
                )
        );

        hotels.add(
                new Hotel(
                        new AggregateId(expectedFirstIdentifier),
                        new HotelName(randomAlphabetic(5)),
                        new HotelDescription(randomAlphabetic(10)),
                        new Location(randomAlphabetic(5)),
                        new Price(nextInt(50, 1000)),
                        new ImageURL("www." + randomAlphabetic(5) + ".com"),
                        new AverageRating(highestAverageRating)
                )
        );

        shuffle(hotels);

        when(aggregateHotelRepository.find(any(Criteria.class))).thenReturn(hotels);

        List<HotelDto> result = testee.get();

        assertThat(result)
                .hasSize(2)
                .satisfies(
                        actualHotels -> {
                            assertThat(actualHotels).first().satisfies(
                                    first -> assertThat(first.identifier()).isEqualTo(expectedFirstIdentifier)
                            );
                            assertThat(actualHotels).last().satisfies(
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
                        aggregateHotelRepository
                )
        );
    }
}