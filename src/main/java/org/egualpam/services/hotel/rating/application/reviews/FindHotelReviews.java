package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.shared.InternalQuery;
import org.egualpam.services.hotel.rating.application.shared.ViewSupplier;
import org.egualpam.services.hotel.rating.domain.reviews.HotelId;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewCriteria;

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
