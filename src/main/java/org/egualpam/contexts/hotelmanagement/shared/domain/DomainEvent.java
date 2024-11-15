package org.egualpam.contexts.hotelmanagement.shared.domain;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

public abstract class DomainEvent {

  private final UniqueId id;
  private final AggregateId aggregateId;
  private final Instant occurredOn;

  public DomainEvent(AggregateId aggregateId) {
    this.id = UniqueId.get();
    this.aggregateId = aggregateId;
    this.occurredOn = Instant.now();
  }

  public DomainEvent(UniqueId id, AggregateId aggregateId, Clock clock) {
    this.id = id;
    this.aggregateId = aggregateId;
    this.occurredOn = clock.instant();
  }

  public UniqueId id() {
    return id;
  }

  public AggregateId aggregateId() {
    return aggregateId;
  }

  public Instant occurredOn() {
    return occurredOn;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DomainEvent that = (DomainEvent) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
