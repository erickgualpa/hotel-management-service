package org.egualpam.services.hotel.rating.application.shared;

@FunctionalInterface
public interface Query<T> {
    T get();
}
