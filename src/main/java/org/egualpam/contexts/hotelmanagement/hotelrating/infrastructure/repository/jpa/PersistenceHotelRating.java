package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.repository.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity(name = "hotel_rating")
class PersistenceHotelRating {

  @Id private UUID id;

  @Column(name = "hotel_id")
  private UUID hotelId;

  @Column(name = "rating_sum")
  private Integer ratingSum;

  @Column(name = "review_count")
  private Integer reviewCount;

  @Column(name = "avg_value")
  private Double avgValue;

  public UUID id() {
    return id;
  }
}
