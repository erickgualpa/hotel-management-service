package org.egualpam.contexts.hotelmanagement.hotel.domain;

import static java.util.Objects.isNull;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;

public final class Hotel extends AggregateRoot {

  private final HotelName name;
  private final HotelDescription description;
  private final Location location;
  private final Price price;
  private final ImageURL imageURL;
  private final AverageRating averageRating;

  public Hotel(
      String id,
      String name,
      String description,
      String location,
      Integer price,
      String imageURL,
      Double averageRating) {
    super(id);
    if (isNull(name)
        || isNull(description)
        || isNull(location)
        || isNull(price)
        || isNull(imageURL)
        || isNull(averageRating)) {
      throw new RequiredPropertyIsMissing();
    }
    this.name = new HotelName(name);
    this.description = new HotelDescription(description);
    this.location = new Location(location);
    this.price = new Price(price);
    this.imageURL = new ImageURL(imageURL);
    this.averageRating = new AverageRating(averageRating);
  }

  public HotelName getName() {
    return name;
  }

  public HotelDescription getDescription() {
    return description;
  }

  public Location getLocation() {
    return location;
  }

  public Price getPrice() {
    return price;
  }

  public ImageURL getImageURL() {
    return imageURL;
  }

  public AverageRating getAverageRating() {
    return averageRating;
  }
}
