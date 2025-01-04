package org.egualpam.contexts.hotelmanagement.shared.domain;

@FunctionalInterface
public interface UniqueIdSupplier {
  UniqueId get();
}
