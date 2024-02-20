package org.egualpam.services.hotel.rating.domain.shared;

import java.util.List;

public interface AggregateRepository<T extends AggregateRoot> {
    T find(AggregateId id);

    List<T> find(Criteria criteria);

    void save(T aggregate);
}
