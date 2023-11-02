package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;

import java.util.List;

public class FindReviewsByHotelIdentifier {

    private final ReviewRepository reviewRepository;

    public FindReviewsByHotelIdentifier(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewDto> execute(String hotelIdentifier) {
        return reviewRepository
                .findByHotelIdentifier(
                        new Identifier(hotelIdentifier)
                )
                .stream()
                .map(review ->
                        new ReviewDto(
                                review.getRating().value(),
                                review.getComment().value()
                        )
                )
                .toList();
    }
}
