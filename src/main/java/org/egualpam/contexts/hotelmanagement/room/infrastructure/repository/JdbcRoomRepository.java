package org.egualpam.contexts.hotelmanagement.room.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.room.domain.Room;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.springframework.jdbc.core.simple.JdbcClient;

public class JdbcRoomRepository implements AggregateRepository<Room> {

  private final JdbcClient jdbcClient;

  public JdbcRoomRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public Optional<Room> find(AggregateId id) {
    throw new RuntimeException("Not yet implemented!");
  }

  @Override
  public void save(Room room) {
    // TODO: Add idempotency to this query as it handles creation and updates
    final var sql =
        """
        INSERT INTO room(id, room_type, hotel_id)
        VALUES(:id, :roomType, :hotelId);
        """
            .trim();

    jdbcClient
        .sql(sql)
        .param("id", UUID.fromString(room.id().value()))
        .param("roomType", room.roomType())
        .param("hotelId", UUID.fromString(room.hotelId()))
        .update();
  }
}
