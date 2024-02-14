package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.shared.Query;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;

import java.util.List;

public class FindHotelReviewsQuery implements Query<List<ReviewDto>> {

    private final String hotelIdentifier;
    private final ReviewRepository reviewRepository;

    public FindHotelReviewsQuery(String hotelIdentifier, ReviewRepository reviewRepository) {
        this.hotelIdentifier = hotelIdentifier;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<ReviewDto> get() {
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
