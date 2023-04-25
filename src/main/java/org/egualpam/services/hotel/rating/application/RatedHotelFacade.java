package org.egualpam.services.hotel.rating.application;

import java.util.List;
import java.util.stream.Collectors;
import org.egualpam.services.hotel.rating.controller.HotelQuery;
import org.egualpam.services.hotel.rating.controller.HotelService;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class RatedHotelFacade implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    public List<RatedHotel> findHotelsMatchingQuery(HotelQuery query) {
        return hotelRepository.findHotelsMatchingQuery(query).stream()
                .map(
                        h ->
                                new RatedHotel(
                                        h.getIdentifier(),
                                        h.getName(),
                                        h.getDescription(),
                                        h.getLocation(),
                                        h.getTotalPrice(),
                                        h.getImageURL()))
                .collect(Collectors.toList());
    }
}
