package org.egualpam.services.hotel.rating.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.egualpam.services.hotel.rating.controller.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RatedHotelFacadeTest {

    @Mock private HotelRepository hotelRepository;
    private RatedHotelFacade testee;

    @BeforeEach
    void setup() {
        testee = new RatedHotelFacade(hotelRepository);
    }

    // TODO: Complete test
    @Test
    void givenQuerySpecifyingLocation_hotelsMatchingLocationShouldBeReturned() {
        HotelQuery query = new HotelQuery("DEFAULT_LOCATION", null, null, null);
        when(hotelRepository.findHotelsMatchingQuery(query)).thenReturn(List.of(new Hotel()));

        List<RatedHotel> result = testee.findHotelsMatchingQuery(query);

        assertThat(result).isNotEmpty();
    }
}