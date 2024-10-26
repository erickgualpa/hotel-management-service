package org.egualpam.contexts.hotelmanagement.shared.application.query;

import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;

@FunctionalInterface
public interface ReadModelSupplier<T extends View> {
  T get(Criteria criteria);
}
