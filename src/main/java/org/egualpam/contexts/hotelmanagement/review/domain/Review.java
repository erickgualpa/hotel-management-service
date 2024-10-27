package org.egualpam.contexts.hotelmanagement.review.domain;

import static java.util.Objects.isNull;

import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;

public final class Review extends AggregateRoot {

  private final HotelId hotelId;
  private final Rating rating;

  private Comment comment;

  public Review(String id, String hotelId, Integer rating, String comment) {
    super(id);
    if (isNull(hotelId) || isNull(rating) || isNull(comment)) {
      throw new RequiredPropertyIsMissing();
    }
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
    if (isNull(id)) {
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

    review.domainEvents().add(new ReviewCreated(review.id()));
    return review;
  }

  public void updateComment(String comment) {
    this.comment = new Comment(comment);
    domainEvents().add(new ReviewUpdated(this.id()));
  }

  public HotelId hotelId() {
    return hotelId;
  }

  public Rating rating() {
    return rating;
  }

  public Comment comment() {
    return comment;
  }
}
