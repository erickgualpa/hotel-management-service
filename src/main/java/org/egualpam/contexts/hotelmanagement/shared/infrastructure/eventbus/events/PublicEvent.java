package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events;

import java.time.Instant;

public interface PublicEvent {
  String getId();

  String getType();

  String getVersion();

  String getAggregateId();

  Instant getOccurredOn();
}
