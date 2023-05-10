package org.egualpam.services.hotel.rating.application;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.egualpam.services.hotel.rating.domain.HotelReview;
import org.egualpam.services.hotel.rating.domain.HotelReviewRepository;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class RatedHotelFacade implements HotelService {

    private final RatedHotelRepository hotelRepository;
    private final HotelReviewRepository reviewRepository;

    @Override
    public List<RatedHotel> findHotelsMatchingQuery(HotelQuery query) {
        List<RatedHotel> hotels = hotelRepository.findHotelsMatchingQuery(query);
        hotels.forEach(this::populateReviews);
        return hotels.stream()
                .sorted(Comparator.comparingDouble(RatedHotel::getRatingAverage).reversed())
                .collect(Collectors.toList());
    }

    private void populateReviews(RatedHotel hotel) {
        List<HotelReview> hotelReviews =
                reviewRepository.findReviewsMatchingHotelIdentifier(hotel.getIdentifier());
        Optional.of(hotelReviews)
                .filter(reviews -> !reviews.isEmpty())
                .ifPresent(hotel::populateReviews);
    }
}
