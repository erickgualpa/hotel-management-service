package org.egualpam.contexts.hotelmanagement.review.domain;

import static java.util.Objects.isNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.review.domain.exceptions.ReviewAlreadyExists;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.exceptions.RequiredPropertyIsMissing;

public final class Review implements AggregateRoot {

  private final AggregateId id;
  private final HotelId hotelId;
  private final Rating rating;
  private final List<DomainEvent> domainEvents = new ArrayList<>();

  private Comment comment;

  public Review(String id, String hotelId, Integer rating, String comment) {
    this.id = new AggregateId(id);
    this.hotelId = new HotelId(hotelId);
    this.rating = new Rating(rating);
    this.comment = new Comment(comment);
  }

  public static Review create(
      AggregateRepository<Review> reviewRepository,
      String id,
      String hotelId,
      Integer rating,
      String comment) {
    if (isNull(id) || isNull(hotelId) || isNull(rating) || isNull(comment)) {
      throw new RequiredPropertyIsMissing();
    }

    Optional.of(id)
        .map(AggregateId::new)
        .flatMap(reviewRepository::find)
        .ifPresent(
            review -> {
              throw new ReviewAlreadyExists();
            });

    Review review = new Review(id, hotelId, rating, comment);

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
