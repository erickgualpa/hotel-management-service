package org.egualpam.services.hotel.rating.domain.hotels;

import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.egualpam.services.hotel.rating.domain.shared.Rating;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Hotel {

    private final Identifier identifier;
    private final HotelName name;
    private final HotelDescription description;
    private final Location location;
    private final Price totalPrice;
    private final ImageURL imageURL;

    public Hotel(
            Identifier identifier,
            HotelName name,
            HotelDescription description,
            Location location,
            Price totalPrice,
            ImageURL imageURL) {
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
                .map(Review::getRating)
                .mapToDouble(Rating::value)
                .filter(Objects::nonNull)
                .average()
                .orElse(0.0);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public HotelName getName() {
        return name;
    }

    public HotelDescription getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    public Price getTotalPrice() {
        return totalPrice;
    }

    public ImageURL getImageURL() {
        return imageURL;
    }
}
