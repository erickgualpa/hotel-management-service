package org.egualpam.contexts.hotelmanagement.hotelrating.domain;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;

import java.time.Clock;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public class HotelRating extends AggregateRoot {

  private final UniqueId hotelId;
  private final Set<UniqueId> reviews;

  private Average average;

  private HotelRating(
      String id, String hotelId, Set<String> reviews, Integer reviewsCount, Double average) {
    super(id);
    if (isNull(hotelId) || isNull(reviewsCount) || isNull(average)) {
      throw new RequiredPropertyIsMissing();
    }

    this.hotelId = new UniqueId(hotelId);
    this.reviews = reviews.stream().map(UniqueId::new).collect(toSet());
    this.average = new Average(average);
  }

  public static HotelRating load(String id, String hotelId, Set<String> reviews, Double average) {
    return new HotelRating(id, hotelId, reviews, reviews.size(), average);
  }

  public static HotelRating initialize(
      HotelRatingIdGenerator hotelRatingIdGenerator,
      AggregateRepository<HotelRating> repository,
      Supplier<UniqueId> uniqueIdSupplier,
      Clock clock,
      String hotelId) {
    var id = hotelRatingIdGenerator.generate(new UniqueId(hotelId));

    Optional.of(id)
        .flatMap(repository::find)
        .ifPresent(
            hotelRating -> {
              throw new HotelRatingAlreadyExists(hotelRating.id());
            });

    var hotelRating = new HotelRating(id.value(), hotelId, Set.of(), 0, 0.0);

    var hotelRatingInitialized =
        new HotelRatingInitialized(uniqueIdSupplier.get(), hotelRating.id(), clock);

    hotelRating.addDomainEvent(hotelRatingInitialized);

    return hotelRating;
  }

  public void update(
      Supplier<UniqueId> uniqueIdSupplier, Clock clock, String reviewId, Integer reviewRating) {
    if (isNull(reviewId) || isNull(reviewRating)) {
      throw new RequiredPropertyIsMissing();
    }

    var reviewUniqueId = new UniqueId(reviewId);

    if (reviews.contains(reviewUniqueId)) {
      throw new ReviewAlreadyProcessed(reviewUniqueId);
    }

    final var reviewsCount = this.reviews.size();
    final var reviewsRatingSum = this.average.calculateRatingSumFor(reviewsCount);

    final int updatedReviewsRatingSum = reviewsRatingSum + reviewRating;
    final int updatedReviewsCount = reviewsCount + 1;

    final var updatedAverage = (double) updatedReviewsRatingSum / updatedReviewsCount;

    this.reviews.add(reviewUniqueId);
    this.average = new Average(updatedAverage);

    final var hotelRatingUpdated = new HotelRatingUpdated(uniqueIdSupplier.get(), this.id(), clock);

    this.addDomainEvent(hotelRatingUpdated);
  }

  public String hotelId() {
    return this.hotelId.value();
  }

  public Set<String> reviews() {
    return this.reviews.stream().map(UniqueId::value).collect(toSet());
  }

  public Double average() {
    return this.average.value();
  }
}
