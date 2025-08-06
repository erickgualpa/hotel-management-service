package org.egualpam.contexts.hotel.management.hotelrating.infrastructure.repository.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity(name = "hotel_rating")
class PersistenceHotelRating {

  @Id private UUID id;

  @Column(name = "hotel_id")
  private UUID hotelId;

  @Column(name = "review_count")
  private Integer reviewCount;

  @Column(name = "avg_value")
  private Double avgValue;

  @Convert(converter = PersistenceReviewsConverter.class)
  @Column(name = "reviews")
  private PersistenceReviews persistenceReviews;

  UUID id() {
    return this.id;
  }

  UUID hotelId() {
    return this.hotelId;
  }

  PersistenceReviews persistenceReviews() {
    return this.persistenceReviews;
  }

  Double average() {
    return this.avgValue;
  }
}
