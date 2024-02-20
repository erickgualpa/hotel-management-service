package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRoot;
import org.egualpam.services.hotel.rating.domain.shared.Comment;
import org.egualpam.services.hotel.rating.domain.shared.DomainEvent;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.egualpam.services.hotel.rating.domain.shared.Rating;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Review implements AggregateRoot {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private final AggregateId aggregateId;
    private final Identifier identifier;
    private final Identifier hotelIdentifier;
    private final Rating rating;
    private Comment comment;

    public Review(
            Identifier identifier,
            Identifier hotelIdentifier,
            Rating rating,
            Comment comment
    ) {
        this.aggregateId = new AggregateId(UUID.fromString(identifier.value()));
        this.identifier = identifier;
        this.hotelIdentifier = hotelIdentifier;
        this.rating = rating;
        this.comment = comment;
        this.domainEvents.add(new ReviewCreated(this.identifier));
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

    public Identifier getIdentifier() {
        return identifier;
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