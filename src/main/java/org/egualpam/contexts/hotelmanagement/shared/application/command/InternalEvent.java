package org.egualpam.contexts.hotelmanagement.shared.application.command;

import java.time.Instant;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public abstract class InternalEvent {

  private final UniqueId id;
  private final AggregateId aggregateId;
  private final Instant occurredOn;

  public InternalEvent(AggregateId aggregateId) {
    this.id = UniqueId.get();
    this.aggregateId = aggregateId;
    this.occurredOn = Instant.now();
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
