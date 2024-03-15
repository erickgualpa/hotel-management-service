package org.egualpam.services.hotel.rating.application.shared;

import org.egualpam.services.hotel.rating.domain.shared.Criteria;

@FunctionalInterface
public interface ViewSupplier<T extends View> {
    T get(Criteria criteria);
}
