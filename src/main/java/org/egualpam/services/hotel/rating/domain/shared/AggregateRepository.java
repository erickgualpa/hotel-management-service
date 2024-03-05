package org.egualpam.services.hotel.rating.domain.shared;

import java.util.List;
import java.util.Optional;

public interface AggregateRepository<T extends AggregateRoot> {
    // TODO: Add integration tests where required
    Optional<T> find(AggregateId id);

    List<T> find(Criteria criteria);

    void save(T aggregate);
}
