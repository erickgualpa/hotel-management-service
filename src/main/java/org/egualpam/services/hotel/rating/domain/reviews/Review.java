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
        this.domainEvents.add(new ReviewCreated(this.getId()));
    }

    @Override
    public AggregateId getId() {
        return this.id;
    }

    @Override
    public List<DomainEvent> getDomainEvents() {
        return domainEvents;
    }

    public void updateComment(Comment comment) {
        this.comment = comment;
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