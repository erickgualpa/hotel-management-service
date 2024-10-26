package org.egualpam.contexts.hotelmanagement.shared.domain;

import java.util.Optional;

public interface AggregateRepository<T extends AggregateRoot> {
  Optional<T> find(AggregateId id);

  void save(T aggregate);
}
