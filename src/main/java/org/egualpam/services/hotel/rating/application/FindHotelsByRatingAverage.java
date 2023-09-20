package org.egualpam.services.hotel.rating.application;

import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.egualpam.services.hotel.rating.domain.ReviewRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FindHotelsByRatingAverage {

    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;

    public FindHotelsByRatingAverage(HotelRepository hotelRepository, ReviewRepository reviewRepository) {
        this.hotelRepository = hotelRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<HotelDto> execute(HotelQuery query) {
        List<Hotel> hotels = hotelRepository.findHotelsMatchingQuery(query);

        hotels.forEach(
                h -> h.addReviews(
                        reviewRepository.findByHotelIdentifier(h.getIdentifier())
                )
        );

        return hotels.stream()
                .sorted(Comparator.comparingDouble(Hotel::calculateRatingAverage).reversed())
                .map(
                        hotel ->
                                new HotelDto(
                                        hotel.getIdentifier(),
                                        hotel.getName(),
                                        hotel.getDescription(),
                                        hotel.getLocation(),
                                        hotel.getTotalPrice(),
                                        hotel.getImageURL(),
                                        hotel.getReviews().stream()
                                                .map(review ->
                                                        new ReviewDto(review.getRating(), review.getComment()))
                                                .collect(Collectors.toList())
                                ))
                .collect(Collectors.toList());
    }
}