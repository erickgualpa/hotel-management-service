package org.egualpam.contexts.hotelmanagement.shared.infrastructure.internaleventbus.spring;

import org.egualpam.contexts.hotelmanagement.shared.application.command.InternalEvent;
import org.egualpam.contexts.hotelmanagement.shared.application.command.InternalEventBus;
import org.springframework.context.ApplicationEventPublisher;

public class SpringInternalEventBus implements InternalEventBus {

  private final ApplicationEventPublisher eventPublisher;

  public SpringInternalEventBus(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Override
  public void publish(InternalEvent event) {}
}
