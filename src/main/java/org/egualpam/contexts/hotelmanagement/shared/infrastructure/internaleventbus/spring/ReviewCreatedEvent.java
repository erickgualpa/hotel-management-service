package org.egualpam.contexts.hotelmanagement.shared.infrastructure.internaleventbus.spring;

import org.springframework.context.ApplicationEvent;

public final class ReviewCreatedEvent extends ApplicationEvent {

  private final String hotelId;
  private final String reviewId;
  private final Integer rating;

  public ReviewCreatedEvent(Object source, String hotelId, String reviewId, Integer rating) {
    super(source);
    this.hotelId = hotelId;
    this.reviewId = reviewId;
    this.rating = rating;
  }

  public String hotelId() {
    return hotelId;
  }

  public String reviewId() {
    return reviewId;
  }

  public Integer rating() {
    return rating;
  }

  @Override
  public String toString() {
    return "ReviewCreatedEvent{"
        + "hotelId='"
        + hotelId
        + '\''
        + ", reviewId='"
        + reviewId
        + '\''
        + ", rating="
        + rating
        + '}';
  }
}
