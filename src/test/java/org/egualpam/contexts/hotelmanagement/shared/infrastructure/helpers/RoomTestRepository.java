package org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers;

import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;

public class RoomTestRepository {

  private final JdbcClient jdbcClient;

  public RoomTestRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
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
}
