package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRoot;
import org.egualpam.services.hotel.rating.domain.shared.DomainEvent;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;

import java.util.ArrayList;
import java.util.List;

public final class Review implements AggregateRoot {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private final AggregateId aggregateId;
    private final Identifier hotelIdentifier;
    private final Rating rating;
    private Comment comment;

    public Review(
            Identifier identifier,
            Identifier hotelIdentifier,
            Rating rating,
            Comment comment
    ) {
        this.aggregateId = new AggregateId(identifier.value());
        this.hotelIdentifier = hotelIdentifier;
        this.rating = rating;
        this.comment = comment;
        this.domainEvents.add(new ReviewCreated(this.getId()));
    }

    @Override
    public AggregateId getId() {
        return this.aggregateId;
    }

    @Override
    public List<DomainEvent> getDomainEvents() {
        return domainEvents;
    }

    public void updateComment(Comment comment) {
        this.comment = comment;
    }

    public Identifier getHotelIdentifier() {
        return hotelIdentifier;
    }

    public Rating getRating() {
        return rating;
    }

    public Comment getComment() {
        return comment;
    }
}