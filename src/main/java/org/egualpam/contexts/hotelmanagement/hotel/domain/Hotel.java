package org.egualpam.contexts.hotelmanagement.hotel.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;

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
    super(new AggregateId(id));
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
