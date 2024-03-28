package org.egualpam.services.hotelmanagement.shared.application.query;

import org.egualpam.services.hotelmanagement.shared.domain.Criteria;

@FunctionalInterface
public interface ViewSupplier<T extends View> {
    T get(Criteria criteria);
}
