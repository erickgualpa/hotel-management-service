package org.egualpam.contexts.hotelmanagement.hotel.domain;

import static java.util.Objects.isNull;

import java.time.Clock;
import java.util.Map;
import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotelmanagement.shared.domain.EntityId;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueIdSupplier;

public final class Hotel extends AggregateRoot {

  private final HotelName name;
  private final HotelDescription description;
  private final Location location;
  private final Price price;
  private final ImageURL imageURL;

  private HotelRating rating = new HotelRating(0, 0.0);

  private Hotel(
      String id, String name, String description, String location, Integer price, String imageURL) {
    super(id);
    if (isNull(name)
        || isNull(description)
        || isNull(location)
        || isNull(price)
        || isNull(imageURL)) {
      throw new RequiredPropertyIsMissing();
    }
    this.name = new HotelName(name);
    this.description = new HotelDescription(description);
    this.location = new Location(location);
    this.price = new Price(price);
    this.imageURL = new ImageURL(imageURL);
  }

  public static Hotel load(Map<String, Object> properties) {
    return new Hotel(
        (String) properties.get("id"),
        (String) properties.get("name"),
        (String) properties.get("description"),
        (String) properties.get("location"),
        (Integer) properties.get("price"),
        (String) properties.get("imageURL"));
  }

  public static Hotel create(
      String id,
      String name,
      String description,
      String location,
      Integer price,
      String imageURL,
      UniqueIdSupplier uniqueIdSupplier,
      AggregateRepository<Hotel> repository,
      Clock clock) {
    Optional.of(id)
        .map(AggregateId::new)
        .flatMap(repository::find)
        .ifPresent(
            hotel -> {
              throw new HotelAlreadyExists(hotel.id());
            });

    Hotel hotel = new Hotel(id, name, description, location, price, imageURL);
    HotelCreated hotelCreated = new HotelCreated(uniqueIdSupplier.get(), hotel.id(), clock);
    hotel.domainEvents().add(hotelCreated);
    return hotel;
  }

  public void updateRating(
      String reviewId,
      Integer reviewRating,
      ReviewIsAlreadyProcessed reviewIsAlreadyProcessed,
      Clock clock,
      UniqueIdSupplier uniqueIdSupplier) {
    if (isNull(reviewId) || isNull(reviewRating)) {
      throw new RequiredPropertyIsMissing();
    }

    EntityId reviewEntityId = new EntityId(reviewId);
    if (reviewIsAlreadyProcessed.with(reviewEntityId)) {
      throw new ReviewAlreadyProcessed(reviewEntityId);
    }

    final Integer reviewsRatingSum = this.rating.ratingSum();
    final Integer reviewsCount = this.rating.reviewsCount();

    final Integer updatedReviewsRatingSum = reviewsRatingSum + reviewRating;
    final Integer updatedReviewsCount = reviewsCount + 1;
    final Double updatedAverage = (double) (updatedReviewsRatingSum / updatedReviewsCount);

    this.rating = new HotelRating(updatedReviewsCount, updatedAverage);

    final HotelRatingUpdated hotelRatingUpdated =
        new HotelRatingUpdated(uniqueIdSupplier.get(), this.id(), reviewEntityId, clock);
    this.domainEvents().add(hotelRatingUpdated);
  }

  public HotelName name() {
    return name;
  }

  public HotelDescription description() {
    return description;
  }

  public Location location() {
    return location;
  }

  public Price price() {
    return price;
  }

  public ImageURL imageURL() {
    return imageURL;
  }

  public HotelRating rating() {
    return rating;
  }
}
