package org.egualpam.services.hotel.rating.domain;

import java.util.List;

public interface HotelReviewRepository {
    List<HotelReview> findReviewsMatchingHotelIdentifier(String hotelIdentifier);
}
