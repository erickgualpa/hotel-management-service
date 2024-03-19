package org.egualpam.services.hotelmanagement.application.shared;

import org.egualpam.services.hotelmanagement.domain.shared.Criteria;

@FunctionalInterface
public interface ViewSupplier<T extends View> {
    T get(Criteria criteria);
}
