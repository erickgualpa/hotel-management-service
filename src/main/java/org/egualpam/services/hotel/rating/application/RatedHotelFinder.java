package org.egualpam.services.hotel.rating.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class RatedHotelFinder implements HotelService {

    private final RatedHotelRepository repository;

    @Override
    public List<Hotel> findByQueryAndSortedByRatingAverage(HotelQuery query) {
        return repository.findHotelsMatchingQuery(query).stream()
                .sorted(Comparator.comparingDouble(Hotel::calculateRatingAverage).reversed())
                .collect(Collectors.toList());
    }
}
