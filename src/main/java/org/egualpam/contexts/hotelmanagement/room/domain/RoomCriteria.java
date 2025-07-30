package org.egualpam.contexts.hotelmanagement.room.domain;

import static java.util.Objects.isNull;

import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.Criteria;
import org.egualpam.contexts.hotelmanagement.shared.domain.RequiredPropertyIsMissing;

public class RoomCriteria implements Criteria {

  private final AggregateId roomId;

  public RoomCriteria(String roomId) {
    if (isNull(roomId)) {
      throw new RequiredPropertyIsMissing();
    }
    this.roomId = new AggregateId(roomId);
  }

  public String getRoomId() {
    return roomId.value();
  }
}
