package org.egualpam.services.hotel.rating.application.shared;

@FunctionalInterface
public interface InternalQuery<T> {
    T get();
}
