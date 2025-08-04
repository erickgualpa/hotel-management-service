package org.egualpam.contexts.hotelmanagement.room.domain;

import static java.util.Objects.isNull;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRoot;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;
import org.egualpam.contexts.hotelmanagement.shared.domain.RoomType;

public class Room extends AggregateRoot {

  private final RoomType type;
  private final AggregateId hotelId;

  private Room(String id, String type, String hotelId) {
    super(id);

    if (isNull(type) || isNull(hotelId)) {
      throw new RequiredPropertyIsMissing();
    }

    this.type = RoomType.valueOf(type);
    this.hotelId = new AggregateId(hotelId);
  }

  public static Room create(String id, String type, String hotelId) {
    return new Room(id, type, hotelId);
  }

  public String roomType() {
    return this.type.name();
  }

  public String hotelId() {
    return this.hotelId.value();
  }
}
