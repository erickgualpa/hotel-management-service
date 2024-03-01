package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRoot;
import org.egualpam.services.hotel.rating.domain.shared.DomainEvent;

import java.util.ArrayList;
import java.util.List;

public final class Review implements AggregateRoot {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private final AggregateId id;
    private final HotelId hotelIdentifier;
    private final Rating rating;
    private Comment comment;

    public Review(
            AggregateId id,
            HotelId hotelIdentifier,
            Rating rating,
            Comment comment
    ) {
        this.id = id;
        this.hotelIdentifier = hotelIdentifier;
        this.rating = rating;
        this.comment = comment;
    }

    @Override
    public AggregateId getId() {
        return this.id;
    }

    @Override
    public List<DomainEvent> getDomainEvents() {
        List<DomainEvent> domainEventsCopy = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();
        return domainEventsCopy;
    }

    public static Review create(
            AggregateId id,
            HotelId hotelIdentifier,
            Rating rating,
            Comment comment
    ) {
        Review review = new Review(id, hotelIdentifier, rating, comment);
        review.domainEvents.add(new ReviewCreated(review));
        return review;
    }

    public void updateComment(Comment comment) {
        this.comment = comment;
        domainEvents.add(new ReviewUpdated(this));
    }

    public HotelId getHotelIdentifier() {
        return hotelIdentifier;
    }

    public Rating getRating() {
        return rating;
    }

    public Comment getComment() {
        return comment;
    }
}