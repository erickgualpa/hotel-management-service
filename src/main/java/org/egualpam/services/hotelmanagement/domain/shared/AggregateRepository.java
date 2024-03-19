package org.egualpam.services.hotelmanagement.domain.shared;

import java.util.Optional;

public interface AggregateRepository<T extends AggregateRoot> {
    Optional<T> find(AggregateId id);

    void save(T aggregate);
}
