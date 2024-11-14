package org.egualpam.contexts.hotelmanagement.shared.domain;

import static java.util.Objects.isNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class AggregateRoot {

  private final AggregateId id;
  private final Set<DomainEvent> domainEvents = new HashSet<>();

  protected AggregateRoot(String id) {
    if (isNull(id)) {
      throw new RequiredPropertyIsMissing();
    }
    this.id = new AggregateId(id);
  }

  public final AggregateId id() {
    return id;
  }

  protected final Set<DomainEvent> domainEvents() {
    return domainEvents;
  }

  public final Set<DomainEvent> pullDomainEvents() {
    Set<DomainEvent> domainEventsCopy = new HashSet<>(domainEvents);
    domainEvents.clear();
    return domainEventsCopy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AggregateRoot that = (AggregateRoot) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
