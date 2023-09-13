package org.egualpam.services.hotel.rating.domain;

import org.egualpam.services.hotel.rating.application.HotelQuery;

import java.util.List;

public abstract class HotelRepository {

    abstract public List<Hotel> findHotelsMatchingQuery(HotelQuery query);

    protected Hotel mapIntoEntity(
            String identifier,
            String name,
            String description,
            String location,
            Integer totalPrice,
            String imageURL,
            List<Review> reviews
    ) {
        Hotel hotel = new Hotel(
                identifier,
                name,
                description,
                location,
                totalPrice,
                imageURL
        );

        hotel.addReviews(reviews);

        return hotel;
    }

    protected Review mapIntoReviewEntity(String identifier, Integer rating, String comment) {
        return new Review(identifier, rating, comment);
    }
}
