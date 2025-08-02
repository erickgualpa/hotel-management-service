package org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers;

import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.room.application.command.CreateRoom;
import org.egualpam.contexts.hotelmanagement.room.application.command.CreateRoomCommand;
import org.springframework.jdbc.core.simple.JdbcClient;

public class RoomTestRepository {

  private final JdbcClient jdbcClient;
  private final CreateRoom createRoom;

  public RoomTestRepository(JdbcClient jdbcClient, CreateRoom createRoom) {
    this.jdbcClient = jdbcClient;
    this.createRoom = createRoom;
  }

  public boolean roomExists(UUID roomId) {
    String sql =
        """
        SELECT COUNT(1)
        FROM room
        WHERE id=:id
        """
            .trim();
    return jdbcClient.sql(sql).param("id", roomId).query(Integer.class).single() == 1;
  }

  public void insertRoom(String id, String type, String hotelId) {
    createRoom.execute(new CreateRoomCommand(id, type, hotelId));
  }

  public void tearDown() {
    String sql =
        """
        TRUNCATE TABLE room CASCADE;
        """;
    jdbcClient.sql(sql).update();
  }
}
