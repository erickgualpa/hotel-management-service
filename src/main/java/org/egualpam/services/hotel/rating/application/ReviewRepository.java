package org.egualpam.services.hotel.rating.application;

import java.util.List;
import org.egualpam.services.hotel.rating.domain.HotelReview;

public interface ReviewRepository {
    List<HotelReview> findReviewsMatchingHotelIdentifier(String hotelIdentifier);
}
