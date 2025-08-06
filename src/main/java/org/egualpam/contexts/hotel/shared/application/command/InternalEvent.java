package org.egualpam.contexts.hotel.shared.application.command;

import java.time.Clock;
import java.time.Instant;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;

public abstract class InternalEvent {

  private final UniqueId id;
  private final AggregateId aggregateId;
  private final Instant occurredOn;

  public InternalEvent(UniqueId id, AggregateId aggregateId, Clock clock) {
    this.id = id;
    this.aggregateId = aggregateId;
    this.occurredOn = clock.instant();
  }

  public final UniqueId id() {
    return id;
  }

  public final AggregateId aggregateId() {
    return aggregateId;
  }

  public final Instant occurredOn() {
    return occurredOn;
  }
}
