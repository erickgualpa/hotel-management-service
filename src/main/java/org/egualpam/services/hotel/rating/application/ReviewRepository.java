package org.egualpam.services.hotel.rating.application;

import java.util.List;
import org.egualpam.services.hotel.rating.domain.Review;

public interface ReviewRepository {
    List<Review> findReviewsMatchingHotelIdentifier(String hotelIdentifier);
}
