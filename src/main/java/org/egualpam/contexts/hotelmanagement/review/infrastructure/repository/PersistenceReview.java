package org.egualpam.contexts.hotelmanagement.review.infrastructure.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity(name = "reviews")
class PersistenceReview {

  @Id private UUID id;
  private Integer rating;
  private String comment;

  @Column(name = "hotel_id")
  private UUID hotelId;

  UUID getId() {
    return id;
  }

  void setId(UUID id) {
    this.id = id;
  }

  Integer getRating() {
    return rating;
  }

  void setRating(Integer rating) {
    this.rating = rating;
  }

  String getComment() {
    return comment;
  }

  void setComment(String comment) {
    this.comment = comment;
  }

  UUID getHotelId() {
    return hotelId;
  }

  void setHotelId(UUID hotelId) {
    this.hotelId = hotelId;
  }
}
