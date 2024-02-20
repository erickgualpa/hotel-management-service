package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.shared.InternalQuery;
import org.egualpam.services.hotel.rating.domain.reviews.HotelId;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewCriteria;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;
import org.egualpam.services.hotel.rating.domain.shared.Criteria;

import java.util.List;

public class FindHotelReviews implements InternalQuery<List<ReviewDto>> {

    private final HotelId hotelId;
    private final AggregateRepository<Review> aggregateReviewRepository;

    public FindHotelReviews(
            String hotelId,
            AggregateRepository<Review> aggregateReviewRepository
    ) {
        this.hotelId = new HotelId(hotelId);
        this.aggregateReviewRepository = aggregateReviewRepository;
    }

    @Override
    public List<ReviewDto> get() {
        Criteria criteria = new ReviewCriteria(hotelId);
        return aggregateReviewRepository.find(criteria)
                .stream()
                .map(review ->
                        new ReviewDto(
                                review.getRating().value(),
                                review.getComment().value()))
                .toList();
    }
}
