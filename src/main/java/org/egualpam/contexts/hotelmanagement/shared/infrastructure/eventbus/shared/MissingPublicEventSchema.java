package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared;

import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared.events.PublicEvent;

public final class MissingPublicEventSchema extends RuntimeException {
  public MissingPublicEventSchema(PublicEvent publicEvent) {
    super("No available schema for event: [%s]".formatted(publicEvent.getClass().getSimpleName()));
  }
}
