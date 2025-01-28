package org.egualpam.contexts.hotelmanagement.hotelrating.domain;

import java.time.Clock;
import java.util.Map;
import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueIdSupplier;

public class HotelRating extends AggregateRoot {
  private HotelRating(String id) {
    super(id);
  }

  public static HotelRating load(Map<String, Object> properties) {
    return new HotelRating((String) properties.get("id"));
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

    HotelRating hotelRating = new HotelRating(id);

    HotelRatingInitialized hotelRatingInitialized =
        new HotelRatingInitialized(uniqueIdSupplier.get(), hotelRating.id(), clock);

    hotelRating.domainEvents().add(hotelRatingInitialized);

    return hotelRating;
  }
}
