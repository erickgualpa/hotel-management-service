package org.egualpam.contexts.hotelmanagement.shared.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "hotels")
public class PersistenceHotel {

  @Id private UUID id;
  private String name;
  private String description;
  private String location;

  @Column(name = "total_price")
  private Integer totalPrice;

  @Column(name = "image_url")
  private String imageURL;

  public PersistenceHotel() {}

  public PersistenceHotel(
      UUID id,
      String name,
      String description,
      String location,
      Integer totalPrice,
      String imageURL) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.location = location;
    this.totalPrice = totalPrice;
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

  public Integer getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Integer totalPrice) {
    this.totalPrice = totalPrice;
  }

  public String getImageURL() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }
}
