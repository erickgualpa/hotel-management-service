package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;

import java.util.List;

public class FindReviews {

    private final ReviewRepository reviewRepository;

    public FindReviews(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewDto> findByHotelIdentifier(String hotelIdentifier) {
        return reviewRepository.findByHotelIdentifier(hotelIdentifier).stream()
                .map(review ->
                        new ReviewDto(
                                review.getRating().value(),
                                review.getComment().value()
                        )
                )
                .toList();
    }
}
