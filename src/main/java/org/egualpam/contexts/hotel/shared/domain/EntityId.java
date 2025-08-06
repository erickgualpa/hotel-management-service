package org.egualpam.contexts.hotel.shared.domain;

import java.util.Objects;

public final class EntityId {

  private final UniqueId value;

  public EntityId(String value) {
    this.value = new UniqueId(value);
  }

  public String value() {
    return value.value();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EntityId that = (EntityId) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
