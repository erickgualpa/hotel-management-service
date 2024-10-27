package org.egualpam.contexts.hotelmanagement.review.domain;

import java.time.Instant;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;

public final class ReviewUpdated implements DomainEvent {

  private final UniqueId id;
  private final AggregateId aggregateId;
  private final Instant occurredOn;

  public ReviewUpdated(AggregateId aggregateId, Instant occurredOn) {
    this.id = UniqueId.get();
    this.aggregateId = aggregateId;
    this.occurredOn = occurredOn;
  }

  @Override
  public UniqueId id() {
    return id;
  }

  @Override
  public AggregateId aggregateId() {
    return this.aggregateId;
  }

  @Override
  public Instant occurredOn() {
    return this.occurredOn;
  }
}
