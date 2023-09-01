package org.egualpam.services.hotel.rating.application;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public final class RatedHotelFinder implements HotelService {

    private final HotelRepository repository;

    @Override
    public List<Hotel> findByQueryAndSortedByRatingAverage(HotelQuery query) {
        return repository.findHotelsMatchingQuery(query).stream()
                .sorted(Comparator.comparingDouble(Hotel::calculateRatingAverage).reversed())
                .collect(Collectors.toList());
    }
}
