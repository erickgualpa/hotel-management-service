package org.egualpam.services.hotel.rating.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class RatedHotelFinder implements HotelService {

    private final RatedHotelRepository repository;

    @Override
    public List<RatedHotel> findByQueryAndSortedByRatingAverage(HotelQuery query) {
        return repository.findHotelsMatchingQuery(query).stream()
                .sorted(Comparator.comparingDouble(RatedHotel::calculateRatingAverage).reversed())
                .collect(Collectors.toList());
    }
}
