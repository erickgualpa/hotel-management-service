package org.egualpam.services.hotel.rating.application;

import java.util.List;
import java.util.stream.Collectors;
import org.egualpam.services.hotel.rating.controller.HotelQuery;
import org.egualpam.services.hotel.rating.controller.HotelService;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.Review;
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
                .collect(Collectors.toList());
    }

    private RatedHotel createRatedHotel(Hotel hotel) {
        RatedHotel ratedHotel =
                new RatedHotel(
                        hotel.getIdentifier(),
                        hotel.getName(),
                        hotel.getDescription(),
                        hotel.getLocation(),
                        hotel.getTotalPrice(),
                        hotel.getImageURL());

        List<Review> hotelReviews =
                reviewRepository.findReviewsMatchingHotelIdentifier(hotel.getIdentifier());

        // TODO: populateReviews()
        /*ratedHotel.populateReviews(hotelReviews);*/

        return ratedHotel;
    }
}
