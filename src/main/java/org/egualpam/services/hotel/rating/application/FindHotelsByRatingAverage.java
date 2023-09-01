package org.egualpam.services.hotel.rating.application;

import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FindHotelsByRatingAverage {

    private final HotelRepository hotelRepository;

    public FindHotelsByRatingAverage(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> execute(HotelQuery query) {
        return hotelRepository.findHotelsMatchingQuery(query).stream()
                .sorted(Comparator.comparingDouble(Hotel::calculateRatingAverage).reversed())
                .collect(Collectors.toList());
    }
}
