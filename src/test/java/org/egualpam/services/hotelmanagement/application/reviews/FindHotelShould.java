package org.egualpam.services.hotelmanagement.application.reviews;

import org.egualpam.services.hotelmanagement.application.hotels.HotelView;
import org.egualpam.services.hotelmanagement.application.shared.ViewSupplier;
import org.egualpam.services.hotelmanagement.domain.shared.Criteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
    private ViewSupplier<HotelView> hotelViewSupplier;

    @Test
    void findHotel() {
        String hotelId = randomUUID().toString();

        HotelView.Hotel hotel = new HotelView.Hotel(
                hotelId,
                randomAlphabetic(5),
                randomAlphabetic(5),
                randomAlphabetic(5),
                nextInt(100, 200),
                "www." + randomAlphabetic(5) + ".com",
                nextDouble(1, 5)
        );

        when(hotelViewSupplier.get(any(Criteria.class))).thenReturn(
                new HotelView(Optional.of(hotel))
        );

        FindHotel testee = new FindHotel(hotelId, hotelViewSupplier);
        HotelView result = testee.get();

        assertNotNull(result);
        assertThat(result.hotel()).isNotEmpty();
        HotelView.Hotel actual = result.hotel().get();
        assertAll(
                () -> assertThat(actual.identifier()).isEqualTo(hotel.identifier()),
                () -> assertThat(actual.name()).isEqualTo(hotel.name()),
                () -> assertThat(actual.description()).isEqualTo(hotel.description()),
                () -> assertThat(actual.location()).isEqualTo(hotel.location()),
                () -> assertThat(actual.totalPrice()).isEqualTo(hotel.totalPrice()),
                () -> assertThat(actual.imageURL()).isEqualTo(hotel.imageURL()),
                () -> assertThat(actual.averageRating()).isEqualTo(hotel.averageRating())
        );
    }
}