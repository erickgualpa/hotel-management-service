package org.egualpam.services.hotelmanagement.domain.shared;

import java.util.List;
import java.util.Optional;

public interface AggregateRepository<T extends AggregateRoot> {
    Optional<T> find(AggregateId id);

    List<T> find(Criteria criteria);

    void save(T aggregate);
}
