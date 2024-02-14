package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.application.shared.Query;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;

import java.util.List;
import java.util.Optional;

public final class HotelQueryAssistant {

    private final HotelRepository hotelRepository;

    public HotelQueryAssistant(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Query<List<HotelDto>> findHotelsQuery(
            Optional<String> locationFilter,
            Optional<Integer> minPriceFilter,
            Optional<Integer> maxPriceFilter
    ) {
        return new FindHotelsQuery(
                locationFilter,
                minPriceFilter,
                maxPriceFilter,
                hotelRepository
        );
    }
}
