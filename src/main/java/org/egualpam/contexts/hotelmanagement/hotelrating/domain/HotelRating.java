package org.egualpam.contexts.hotelmanagement.hotelrating.domain;

import static java.util.Objects.isNull;

import java.time.Clock;
import java.util.Map;
import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueIdSupplier;

public class HotelRating extends AggregateRoot {

  private final UniqueId hotelId;
  private ReviewsCount reviewsCount;
  private Average average;

  private HotelRating(String id, String hotelId, Integer reviewsCount, Double average) {
    super(id);
    if (isNull(hotelId) || isNull(reviewsCount) || isNull(average)) {
      throw new RequiredPropertyIsMissing();
    }

    this.hotelId = new UniqueId(hotelId);
    this.reviewsCount = new ReviewsCount(reviewsCount);
    this.average = new Average(average);
  }

  public static HotelRating load(Map<String, Object> properties) {
    return new HotelRating(
        (String) properties.get("id"),
        (String) properties.get("hotelId"),
        (Integer) properties.get("reviewsCount"),
        (Double) properties.get("average"));
  }

  public static HotelRating initialize(
      AggregateRepository<HotelRating> repository,
      UniqueIdSupplier uniqueIdSupplier,
      Clock clock,
      String id,
      String hotelId) {
    Optional.of(id)
        .map(AggregateId::new)
        .flatMap(repository::find)
        .ifPresent(
            hotelRating -> {
              throw new HotelRatingAlreadyExists(hotelRating.id());
            });

    HotelRating hotelRating = new HotelRating(id, hotelId, 0, 0.0);

    HotelRatingInitialized hotelRatingInitialized =
        new HotelRatingInitialized(uniqueIdSupplier.get(), hotelRating.id(), clock);

    hotelRating.domainEvents().add(hotelRatingInitialized);

    return hotelRating;
  }

  // TODO: Make this idempotent
  // TODO: Integrate into use case
  public void update(
      UniqueIdSupplier uniqueIdSupplier, Clock clock, String reviewId, Integer reviewRating) {
    if (isNull(reviewId) || isNull(reviewRating)) {
      throw new RequiredPropertyIsMissing();
    }

    final Integer reviewsRatingSum = this.ratingSum();
    final Integer reviewsCount = this.reviewsCount();

    final int updatedReviewsRatingSum = reviewsRatingSum + reviewRating;
    final int updatedReviewsCount = reviewsCount + 1;

    final Double updatedAverage = (double) updatedReviewsRatingSum / updatedReviewsCount;

    this.reviewsCount = new ReviewsCount(updatedReviewsCount);
    this.average = new Average(updatedAverage);

    final HotelRatingUpdated hotelRatingUpdated =
        new HotelRatingUpdated(uniqueIdSupplier.get(), this.id(), clock);

    this.domainEvents().add(hotelRatingUpdated);
  }

  public String hotelId() {
    return this.hotelId.value();
  }

  public Integer reviewsCount() {
    return this.reviewsCount.value();
  }

  public Double average() {
    return this.average.value();
  }

  public Integer ratingSum() {
    return this.average.calculateRatingSumFor(this.reviewsCount);
  }
}
