package org.egualpam.services.hotel.rating.application.shared;

@FunctionalInterface
public interface InternalQuery<T extends View> {
    T get();
}
