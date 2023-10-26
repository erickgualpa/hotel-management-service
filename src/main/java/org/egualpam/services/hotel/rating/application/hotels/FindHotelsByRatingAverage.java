package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.application.reviews.ReviewDto;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;

import java.util.Comparator;
import java.util.List;

public class FindHotelsByRatingAverage {

    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;

    public FindHotelsByRatingAverage(HotelRepository hotelRepository, ReviewRepository reviewRepository) {
        this.hotelRepository = hotelRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<HotelDto> execute(HotelQuery query) {
        List<Hotel> hotels = hotelRepository.findHotelsMatchingQuery(query);

        hotels.forEach(this::decorateReviews);

        return hotels.stream()
                .sorted(Comparator.comparingDouble(Hotel::calculateRatingAverage).reversed())
                .map(this::mapIntoHotelDto)
                .toList();
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