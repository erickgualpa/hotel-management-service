package org.egualpam.contexts.hotelmanagement.shared.domain;

import java.util.Objects;
import java.util.UUID;

public final class UniqueId {

  private final String value;

  public UniqueId(String value) {
    this.value = valid(value).toString();
  }

  private static UUID valid(String value) {
    try {
      return UUID.fromString(value);
    } catch (Exception e) {
      throw new InvalidUniqueId(e);
    }
  }

  public static UniqueId get() {
    return new UniqueId(UUID.randomUUID().toString());
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UniqueId uniqueId = (UniqueId) o;
    return Objects.equals(value, uniqueId.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
