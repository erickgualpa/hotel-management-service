package org.egualpam.contexts.hotelmanagement.room.infrastructure.readmodelsupplier;

import java.sql.Date;
import org.egualpam.contexts.hotelmanagement.room.application.query.ManyRooms;
import org.egualpam.contexts.hotelmanagement.room.domain.RoomCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;

public class JdbcManyRoomsReadModelSupplier implements ReadModelSupplier<RoomCriteria, ManyRooms> {

  private final JdbcClient jdbcClient;

  public JdbcManyRoomsReadModelSupplier(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public ManyRooms get(RoomCriteria criteria) {
    final var paramSource = new MapSqlParameterSource();

    var sql =
        """
        SELECT r.id
        FROM room r
        """;

    if (criteria.getAvailableTo() != null && criteria.getAvailableTo() != null) {
      sql +=
          """
          WHERE r.id NOT IN (
            SELECT DISTINCT rv.room_id
            FROM reservation rv
            WHERE (rv.reserved_from, rv.reserved_to) OVERLAPS (:availableFrom, :availableTo)
          )
          """;

      paramSource.addValue("availableFrom", Date.valueOf(criteria.getAvailableFrom()));
      paramSource.addValue("availableTo", Date.valueOf(criteria.getAvailableTo()));
    }

    final var rooms =
        jdbcClient.sql(sql).paramSource(paramSource).query(String.class).list().stream()
            .map(ManyRooms.Room::new)
            .toList();

    return new ManyRooms(rooms);
  }
}
