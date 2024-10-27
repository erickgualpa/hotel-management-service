package org.egualpam.contexts.hotelmanagement.shared.domain;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AggregateRoot {

  private final AggregateId id;
  private final List<DomainEvent> domainEvents = new ArrayList<>();

  protected AggregateRoot(String id) {
    if (isNull(id)) {
      throw new RequiredPropertyIsMissing();
    }
    this.id = new AggregateId(id);
  }

  public final AggregateId id() {
    return id;
  }

  protected final List<DomainEvent> domainEvents() {
    return domainEvents;
  }

  public final List<DomainEvent> pullDomainEvents() {
    List<DomainEvent> domainEventsCopy = new ArrayList<>(domainEvents);
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
