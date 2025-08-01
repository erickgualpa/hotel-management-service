package org.egualpam.contexts.hotelmanagement.room.infrastructure.readmodelsupplier;

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
    final var sql =
        """
        SELECT id
        FROM room
        """
            .trim();

    final var rooms =
        jdbcClient.sql(sql).query(String.class).list().stream().map(ManyRooms.Room::new).toList();

    return new ManyRooms(rooms);
  }
}
