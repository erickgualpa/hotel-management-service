package org.egualpam.contexts.hotelmanagement.room.infrastructure.readmodelsupplier;

import java.util.List;
import org.egualpam.contexts.hotelmanagement.room.application.query.ManyRooms;
import org.egualpam.contexts.hotelmanagement.room.domain.RoomCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.springframework.jdbc.core.simple.JdbcClient;

public class JdbcManyRoomsReadModelSupplier implements ReadModelSupplier<RoomCriteria, ManyRooms> {

  private final JdbcClient jdbcClient;

  public JdbcManyRoomsReadModelSupplier(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public ManyRooms get(RoomCriteria criteria) {
    ManyRooms.Room dummyRoom = new ManyRooms.Room("d6e8ae57-2563-4dfb-ab31-7b6996fd7908");
    return new ManyRooms(List.of(dummyRoom));
  }
}
