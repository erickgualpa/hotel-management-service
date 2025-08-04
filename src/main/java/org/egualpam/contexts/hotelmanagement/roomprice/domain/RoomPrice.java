package org.egualpam.contexts.hotelmanagement.roomprice.domain;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;

public class RoomPrice extends AggregateRoot {
  private RoomPrice(String id) {
    super(id);
  }

  public static RoomPrice create(String id) {
    return new RoomPrice(id);
  }
}
