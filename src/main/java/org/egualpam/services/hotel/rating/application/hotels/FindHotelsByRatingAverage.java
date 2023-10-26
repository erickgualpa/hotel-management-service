package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.application.reviews.ReviewDto;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.egualpam.services.hotel.rating.application.hotels.HotelFilters.LOCATION;
import static org.egualpam.services.hotel.rating.application.hotels.HotelFilters.PRICE_RANGE_BEGIN;
import static org.egualpam.services.hotel.rating.application.hotels.HotelFilters.PRICE_RANGE_END;

public class FindHotelsByRatingAverage {

    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;

    public FindHotelsByRatingAverage(HotelRepository hotelRepository, ReviewRepository reviewRepository) {
        this.hotelRepository = hotelRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<HotelDto> execute(Map<String, String> hotelFilters) {
        List<Hotel> hotels = hotelRepository.findHotelsMatchingQuery(
                buildHotelQuery(hotelFilters)
        );

        hotels.forEach(this::decorateReviews);

        return hotels.stream()
                .sorted(Comparator.comparingDouble(Hotel::calculateRatingAverage).reversed())
                .map(this::mapIntoHotelDto)
                .toList();
    }

    private HotelQuery buildHotelQuery(Map<String, String> hotelFilters) {
        return HotelQuery.create()
                .withLocation(hotelFilters.get(LOCATION.getValue()))
                .withPriceRange(
                        Optional.ofNullable(
                                        hotelFilters.get(PRICE_RANGE_BEGIN.getValue())
                                ).map(Integer::parseInt)
                                .orElse(null),
                        Optional.ofNullable(
                                        hotelFilters.get(PRICE_RANGE_END.getValue())
                                ).map(Integer::parseInt)
                                .orElse(null)
                ).build();
    }

    private void decorateReviews(Hotel hotel) {
        hotel.addReviews(reviewRepository.findByHotelIdentifier(hotel.getIdentifier()));
    }

    private HotelDto mapIntoHotelDto(Hotel hotel) {
        return new HotelDto(
                hotel.getIdentifier(),
                hotel.getName(),
                hotel.getDescription(),
                hotel.getLocation(),
                hotel.getTotalPrice(),
                hotel.getImageURL(),
                hotel.getReviews().stream()
                        .map(review ->
                                new ReviewDto(
                                        review.getRating(),
                                        review.getComment()
                                )
                        ).toList()
        );
    }
}