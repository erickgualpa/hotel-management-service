package org.egualpam.services.hotelmanagement.application.reviews;

import org.egualpam.services.hotelmanagement.application.shared.InternalQuery;
import org.egualpam.services.hotelmanagement.application.shared.ViewSupplier;
import org.egualpam.services.hotelmanagement.domain.reviews.HotelId;
import org.egualpam.services.hotelmanagement.domain.reviews.ReviewCriteria;

public class FindHotelReviews implements InternalQuery<ReviewsView> {

    private final HotelId hotelId;
    private final ViewSupplier<ReviewsView> reviewsViewSupplier;

    public FindHotelReviews(
            String hotelId,
            ViewSupplier<ReviewsView> reviewsViewSupplier
    ) {
        this.hotelId = new HotelId(hotelId);
        this.reviewsViewSupplier = reviewsViewSupplier;
    }

    @Override
    public ReviewsView get() {
        return reviewsViewSupplier.get(
                new ReviewCriteria(hotelId)
        );
    }
}
