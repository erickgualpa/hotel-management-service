package org.egualpam.services.hotel.rating.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.egualpam.services.hotel.rating.controller.HotelQuery;
import org.egualpam.services.hotel.rating.controller.HotelService;
import org.egualpam.services.hotel.rating.domain.HotelReview;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class RatedHotelFacade implements HotelService {

    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public List<RatedHotel> findHotelsMatchingQuery(HotelQuery query) {
        return hotelRepository.findHotelsMatchingQuery(query).stream()
                .map(this::createRatedHotel)
                .sorted(Comparator.comparingDouble(RatedHotel::getRatingAverage).reversed())
                .collect(Collectors.toList());
    }

    private RatedHotel createRatedHotel(RatedHotel hotel) {
        List<HotelReview> hotelReviews =
                reviewRepository.findReviewsMatchingHotelIdentifier(hotel.getIdentifier()).stream()
                        .map(r -> new HotelReview(r.getIdentifier(), r.getRating(), r.getComment()))
                        .collect(Collectors.toList());

        hotel.populateReviews(hotelReviews);

        return hotel;
    }
}
