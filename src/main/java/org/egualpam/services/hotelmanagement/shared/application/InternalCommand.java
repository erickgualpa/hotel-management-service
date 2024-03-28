package org.egualpam.services.hotelmanagement.shared.application;

@FunctionalInterface
public interface InternalCommand {
    void execute();
}
