package org.egualpam.services.hotel.rating.infrastructure.persistance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class StaticReviewRepository {

    private static final List<
                    org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review>
            inMemoryReviews = new ArrayList<>();

    static {
        inMemoryReviews.add(
                new org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review(
                        "AMZ_REVIEW_1", 5, "Eloquent comment", "AMZ_HOTEL"));
        inMemoryReviews.add(
                new org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review(
                        "AMZ_REVIEW_2", 3, "Eloquent comment", "AMZ_HOTEL"));

        inMemoryReviews.add(
                new org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review(
                        "AMZ_REVIEW_3", 1, "Eloquent comment", "AMZ_ROME_HOTEL"));
        inMemoryReviews.add(
                new org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review(
                        "AMZ_REVIEW_4", 3, "Eloquent comment", "AMZ_ROME_HOTEL"));

        inMemoryReviews.add(
                new org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review(
                        "AMZ_REVIEW_5", 5, "Eloquent comment", "AMZ_BERLIN_HOTEL"));
        inMemoryReviews.add(
                new org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review(
                        "AMZ_REVIEW_6", 2, "Eloquent comment", "AMZ_BERLIN_HOTEL"));
        inMemoryReviews.add(
                new org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review(
                        "AMZ_REVIEW_7", 1, "Eloquent comment", "AMZ_BERLIN_HOTEL"));
        inMemoryReviews.add(
                new org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review(
                        "AMZ_REVIEW_8", 1, "Eloquent comment", "AMZ_BERLIN_HOTEL"));

        inMemoryReviews.add(
                new org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review(
                        "AMZ_REVIEW_9", 1, "Eloquent comment", "NON_AMZ_HOTEL"));
        inMemoryReviews.add(
                new org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review(
                        "AMZ_REVIEW_11", 2, "Eloquent comment", "NON_AMZ_HOTEL"));
        inMemoryReviews.add(
                new org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review(
                        "AMZ_REVIEW_12", 1, "Eloquent comment", "NON_AMZ_HOTEL"));

        inMemoryReviews.add(
                new org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review(
                        "AMZ_REVIEW_13", 4, "Eloquent comment", "MEDIUM_AMZ_HOTEL"));
        inMemoryReviews.add(
                new org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review(
                        "AMZ_REVIEW_14", 4, "Eloquent comment", "MEDIUM_AMZ_HOTEL"));
        inMemoryReviews.add(
                new org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Review(
                        "AMZ_REVIEW_15", 3, "Eloquent comment", "MEDIUM_AMZ_HOTEL"));
    }

    public List<Review> findReviewsMatchingHotelIdentifier(String hotelIdentifier) {
        return inMemoryReviews.stream()
                .filter(r -> r.getHotelIdentifier().equals(hotelIdentifier))
                .collect(Collectors.toList());
    }
}