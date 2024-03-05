package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.hotels.HotelView;
import org.egualpam.services.hotel.rating.domain.hotels.AverageRating;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelDescription;
import org.egualpam.services.hotel.rating.domain.hotels.HotelName;
import org.egualpam.services.hotel.rating.domain.hotels.ImageURL;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextDouble;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindHotelShould {

    @Mock
    private AggregateRepository<Hotel> aggregateHotelRepository;

    @Test
    void findHotel() {
        String hotelId = randomUUID().toString();

        Hotel hotel = new Hotel(
                new AggregateId(hotelId),
                new HotelName(randomAlphabetic(5)),
                new HotelDescription(randomAlphabetic(5)),
                new Location(randomAlphabetic(5)),
                new Price(nextInt(100, 200)),
                new ImageURL("www." + randomAlphabetic(5) + ".com"),
                new AverageRating(nextDouble(1, 5)
                )
        );

        when(aggregateHotelRepository.find(any(AggregateId.class))).thenReturn(hotel);

        FindHotel testee = new FindHotel(hotelId, aggregateHotelRepository);
        HotelView result = testee.get();

        assertNotNull(result);
        HotelView.Hotel actual = result.hotel();
        assertAll(
                () -> assertThat(actual.identifier()).isEqualTo(hotel.getId().value().toString()),
                () -> assertThat(actual.name()).isEqualTo(hotel.getName().value()),
                () -> assertThat(actual.description()).isEqualTo(hotel.getDescription().value()),
                () -> assertThat(actual.location()).isEqualTo(hotel.getLocation().value()),
                () -> assertThat(actual.totalPrice()).isEqualTo(hotel.getTotalPrice().value()),
                () -> assertThat(actual.imageURL()).isEqualTo(hotel.getImageURL().value()),
                () -> assertThat(actual.averageRating()).isEqualTo(hotel.getAverageRating().value())
        );
    }
}