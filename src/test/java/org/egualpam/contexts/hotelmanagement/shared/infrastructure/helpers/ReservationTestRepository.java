package org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers;

import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;

public class ReservationTestRepository {

  private final JdbcClient jdbcClient;

  public ReservationTestRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public boolean reservationExists(UUID reservationId) {
    String sql =
        """
        SELECT COUNT(1)
        FROM reservation
        WHERE id=:reservationId
        """;

    Integer count =
        jdbcClient.sql(sql).param("reservationId", reservationId).query(Integer.class).single();

    return count == 1;
  }
}
