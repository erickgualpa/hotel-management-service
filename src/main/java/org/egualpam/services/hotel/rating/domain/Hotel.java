package org.egualpam.services.hotel.rating.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Hotel {

    private final String identifier;
    private final String name;
    private final String description;
    private final Location location;
    private final Integer totalPrice;
    private final String imageURL;

    public Hotel(
            String identifier,
            String name,
            String description,
            Location location,
            Integer totalPrice,
            String imageURL) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.location = location;
        this.totalPrice = totalPrice;
        this.imageURL = imageURL;
    }

    private final List<Review> reviews = new ArrayList<>();

    public void addReviews(List<Review> reviews) {
        this.reviews.addAll(reviews);
    }

    public Double calculateRatingAverage() {
        return this.reviews.stream()
                .mapToDouble(Review::getRating)
                .filter(Objects::nonNull)
                .average()
                .orElse(0.0);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public String getImageURL() {
        return imageURL;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
