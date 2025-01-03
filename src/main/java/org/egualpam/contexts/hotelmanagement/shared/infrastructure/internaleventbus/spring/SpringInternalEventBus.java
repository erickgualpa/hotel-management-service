package org.egualpam.contexts.hotelmanagement.shared.infrastructure.internaleventbus.spring;

import org.egualpam.contexts.hotelmanagement.review.application.command.ReviewCreated;
import org.egualpam.contexts.hotelmanagement.shared.application.command.InternalEvent;
import org.egualpam.contexts.hotelmanagement.shared.application.command.InternalEventBus;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

public class SpringInternalEventBus implements InternalEventBus {

  private final ApplicationEventPublisher eventPublisher;

  public SpringInternalEventBus(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Override
  public void publish(InternalEvent event) {
    if (event instanceof ReviewCreated reviewCreated) {
      String hotelId = reviewCreated.hotelId().value();
      String reviewId = reviewCreated.aggregateId().value();
      Integer rating = reviewCreated.rating().value();

      ApplicationEvent applicationEvent = new ReviewCreatedEvent(this, hotelId, reviewId, rating);

      eventPublisher.publishEvent(applicationEvent);
    }
  }
}
