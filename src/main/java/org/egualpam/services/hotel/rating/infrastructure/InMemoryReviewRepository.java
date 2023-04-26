package org.egualpam.services.hotel.rating.infrastructure;

import java.util.ArrayList;
import java.util.List;
import org.egualpam.services.hotel.rating.application.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.HotelReview;
import org.springframework.stereotype.Component;

@Component
public class InMemoryReviewRepository implements ReviewRepository {

    @Override
    public List<HotelReview> findReviewsMatchingHotelIdentifier(String hotelIdentifier) {
        return new ArrayList<>();
    }
}