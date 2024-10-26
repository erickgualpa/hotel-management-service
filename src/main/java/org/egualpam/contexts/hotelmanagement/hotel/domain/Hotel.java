package org.egualpam.contexts.hotelmanagement.hotel.domain;

import java.util.ArrayList;
import java.util.List;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;

public final class Hotel implements AggregateRoot {

  private final List<DomainEvent> domainEvents = new ArrayList<>();

  private final AggregateId id;
  private final HotelName name;
  private final HotelDescription description;
  private final Location location;
  // TODO: Rename this into 'pricePerNight'
  private final Price totalPrice;
  private final ImageURL imageURL;
  private final AverageRating averageRating;

  public Hotel(
      String id,
      String name,
      String description,
      String location,
      Integer totalPrice,
      String imageURL,
      Double averageRating) {
    this.id = new AggregateId(id);
    this.name = new HotelName(name);
    this.description = new HotelDescription(description);
    this.location = new Location(location);
    this.totalPrice = new Price(totalPrice);
    this.imageURL = new ImageURL(imageURL);
    this.averageRating = new AverageRating(averageRating);
  }

  @Override
  public AggregateId getId() {
    return id;
  }

  @Override
  public List<DomainEvent> pullDomainEvents() {
    List<DomainEvent> domainEventsCopy = new ArrayList<>(this.domainEvents);
    this.domainEvents.clear();
    return domainEventsCopy;
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

  public Price getTotalPrice() {
    return totalPrice;
  }

  public ImageURL getImageURL() {
    return imageURL;
  }

  public AverageRating getAverageRating() {
    return averageRating;
  }
}
