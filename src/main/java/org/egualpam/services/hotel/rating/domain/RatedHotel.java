package org.egualpam.services.hotel.rating.domain;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
// TODO: Separate this entity from web layer
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RatedHotel {

    private String identifier;
    private String name;
    private String description;
    private HotelLocation location;
    private Integer totalPrice;
    private String imageURL;
    private final List<HotelReview> reviews = new ArrayList<>();

    @JsonIgnore private Double ratingAverage = 0.0;

    public RatedHotel() {}

    public RatedHotel(
            String identifier,
            String name,
            String description,
            HotelLocation location,
            Integer totalPrice,
            String imageURL) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.location = location;
        this.totalPrice = totalPrice;
        this.imageURL = imageURL;
    }

    public void populateReviews(List<HotelReview> reviews) {
        this.reviews.addAll(reviews);
        this.reviews.stream()
                .mapToDouble(HotelReview::getRating)
                .average()
                .ifPresent(this::setRatingAverage);
    }

    public Double getRatingAverage() {
        return ratingAverage;
    }

    private void setRatingAverage(Double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }
}
