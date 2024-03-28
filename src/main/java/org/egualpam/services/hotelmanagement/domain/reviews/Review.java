package org.egualpam.services.hotelmanagement.domain.reviews;

import org.egualpam.services.hotelmanagement.domain.shared.AggregateId;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateRoot;
import org.egualpam.services.hotelmanagement.domain.shared.DomainEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class Review implements AggregateRoot {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private final AggregateId id;
    private final HotelId hotelId;
    private final Rating rating;
    private Comment comment;

    public Review(
            String id,
            String hotelId,
            Integer rating,
            String comment
    ) {
        this.id = new AggregateId(id);
        this.hotelId = new HotelId(hotelId);
        this.rating = new Rating(rating);
        this.comment = new Comment(comment);
    }

    public static Review create(
            String id,
            String hotelId,
            Integer rating,
            String comment
    ) {
        Review review = new Review(
                id,
                hotelId,
                rating,
                comment
        );
        review.domainEvents.add(new ReviewCreated(review.id, Instant.now()));
        return review;
    }

    @Override
    public AggregateId getId() {
        return this.id;
    }

    @Override
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> domainEventsCopy = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();
        return domainEventsCopy;
    }

    public void updateComment(Comment comment) {
        this.comment = comment;
        domainEvents.add(new ReviewUpdated(this.id, Instant.now()));
    }

    public HotelId getHotelId() {
        return hotelId;
    }

    public Rating getRating() {
        return rating;
    }

    public Comment getComment() {
        return comment;
    }
}