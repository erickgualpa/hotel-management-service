package org.egualpam.contexts.hotelmanagement.shared.infrastructure.internaleventbus.spring;

import org.springframework.context.ApplicationEvent;

public final class ReviewCreatedEvent extends ApplicationEvent {

  private final String hotelId;
  private final Integer rating;

  public ReviewCreatedEvent(Object source, String hotelId, Integer rating) {
    super(source);
    this.hotelId = hotelId;
    this.rating = rating;
  }

  public String hotelId() {
    return hotelId;
  }

  public Integer rating() {
    return rating;
  }
}
