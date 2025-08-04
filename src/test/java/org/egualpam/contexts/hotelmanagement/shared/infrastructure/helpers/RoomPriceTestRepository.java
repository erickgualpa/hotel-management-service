package org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers;

import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;

public class RoomPriceTestRepository {

  private final JdbcClient jdbcClient;

  public RoomPriceTestRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public boolean roomPriceExists(String roomPriceId) {
    String sql =
        """
        SELECT COUNT(1)
        FROM room_price
        WHERE id=:roomPriceId
        """;

    Integer count =
        jdbcClient
            .sql(sql)
            .param("roomPriceId", UUID.fromString(roomPriceId))
            .query(Integer.class)
            .single();

    return count == 1;
  }
}
