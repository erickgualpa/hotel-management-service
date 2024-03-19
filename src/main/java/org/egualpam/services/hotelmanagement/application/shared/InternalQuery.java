package org.egualpam.services.hotelmanagement.application.shared;

@FunctionalInterface
public interface InternalQuery<T extends View> {
    T get();
}
