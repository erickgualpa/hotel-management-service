package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.shared.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity(name = "hotels")
public class PersistenceHotel {

  @Id private UUID id;
  private String name;
  private String description;
  private String location;
  private Integer price;

  @Column(name = "image_url")
  private String imageURL;

  public PersistenceHotel() {}

  public PersistenceHotel(
      UUID id, String name, String description, String location, Integer price, String imageURL) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.location = location;
    this.price = price;
    this.imageURL = imageURL;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public String getImageURL() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }
}
