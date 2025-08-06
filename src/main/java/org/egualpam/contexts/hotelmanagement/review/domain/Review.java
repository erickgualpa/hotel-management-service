package org.egualpam.contexts.hotelmanagement.review.domain;

import static java.util.Objects.isNull;

import java.time.Clock;
import java.util.Optional;
import java.util.function.Supplier;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotel.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;

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
      String comment,
      Clock clock,
      Supplier<UniqueId> uniqueIdSupplier) {
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

    ReviewCreated reviewCreated =
        new ReviewCreated(
            uniqueIdSupplier.get(), review.id(), review.hotelId, review.rating, clock);
    review.addDomainEvent(reviewCreated);
    return review;
  }

  public void updateComment(String comment, Clock clock, Supplier<UniqueId> uniqueIdSupplier) {
    Optional.ofNullable(comment)
        .map(Comment::new)
        .filter(c -> !c.equals(this.comment))
        .ifPresent(
            c -> {
              this.comment = c;
              ReviewUpdated reviewUpdated =
                  new ReviewUpdated(uniqueIdSupplier.get(), this.id(), clock);
              addDomainEvent(reviewUpdated);
            });
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
