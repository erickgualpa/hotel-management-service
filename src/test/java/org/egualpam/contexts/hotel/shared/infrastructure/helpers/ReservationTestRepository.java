package org.egualpam.contexts.hotel.shared.infrastructure.helpers;

import java.util.UUID;
import org.egualpam.contexts.hotel.management.reservation.application.command.CreateReservation;
import org.egualpam.contexts.hotel.management.reservation.application.command.CreateReservationCommand;
import org.springframework.jdbc.core.simple.JdbcClient;

public class ReservationTestRepository {

  private final JdbcClient jdbcClient;
  private final CreateReservation createReservation;

  public ReservationTestRepository(JdbcClient jdbcClient, CreateReservation createReservation) {
    this.jdbcClient = jdbcClient;
    this.createReservation = createReservation;
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

  public void insertReservation(
      String reservationId, String roomId, String reservedFrom, String reservedTo) {
    createReservation.execute(
        new CreateReservationCommand(reservationId, roomId, reservedFrom, reservedTo));
  }

  public void tearDown() {
    String sql =
        """
        TRUNCATE TABLE reservation CASCADE;
        """;
    jdbcClient.sql(sql).update();
  }
}
