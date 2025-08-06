package org.egualpam.contexts.hotel.shared.application.query;

import org.egualpam.contexts.hotel.shared.domain.Criteria;

@FunctionalInterface
public interface ReadModelSupplier<T extends Criteria, U extends ReadModel> {
  U get(T criteria);
}
