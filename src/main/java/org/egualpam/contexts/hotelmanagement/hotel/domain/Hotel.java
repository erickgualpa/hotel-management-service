package org.egualpam.contexts.hotelmanagement.hotel.domain;

import static java.util.Objects.isNull;

import java.time.Clock;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public final class Hotel extends AggregateRoot {

  private final HotelName name;
  private final HotelDescription description;
  private final Location location;
  private final Price price;
  private final ImageURL imageURL;

  public Hotel(
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

  public static Hotel create(
      String id,
      String name,
      String description,
      String location,
      Integer price,
      String imageURL,
      Clock clock) {
    Hotel hotel = new Hotel(id, name, description, location, price, imageURL);
    HotelCreated hotelCreated = new HotelCreated(UniqueId.get(), hotel.id(), clock);
    hotel.domainEvents().add(hotelCreated);
    return hotel;
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
}
