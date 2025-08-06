package org.egualpam.contexts.hotel.management.hotel.domain;

import static java.util.Objects.isNull;

import java.time.Clock;
import java.util.Optional;
import java.util.function.Supplier;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotel.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;

public final class Hotel extends AggregateRoot {

  private final HotelName name;
  private final HotelDescription description;
  private final Location location;
  private final Price price; // TODO: Extract price from hotel entity
  private final ImageURL imageURL;

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

  public static Hotel load(
      String id, String name, String description, String location, Integer price, String imageURL) {
    return new Hotel(id, name, description, location, price, imageURL);
  }

  public static Hotel create(
      String id,
      String name,
      String description,
      String location,
      Integer price,
      String imageURL,
      Supplier<UniqueId> uniqueIdSupplier,
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
    hotel.addDomainEvent(hotelCreated);
    return hotel;
  }

  public String name() {
    return name.value();
  }

  public String description() {
    return description.value();
  }

  public String location() {
    return location.value();
  }

  public Integer price() {
    return price.value();
  }

  public String imageURL() {
    return imageURL.value();
  }
}
