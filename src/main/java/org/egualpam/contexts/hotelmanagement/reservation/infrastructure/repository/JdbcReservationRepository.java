package org.egualpam.contexts.hotelmanagement.reservation.infrastructure.repository;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;
import org.egualpam.contexts.hotelmanagement.reservation.domain.Reservation;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.springframework.jdbc.core.simple.JdbcClient;

public class JdbcReservationRepository implements AggregateRepository<Reservation> {

  private final JdbcClient jdbcClient;

  public JdbcReservationRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public Optional<Reservation> find(AggregateId id) {
    throw new RuntimeException("Not yet implemented!");
  }

  @Override
  public void save(Reservation reservation) {
    // TODO: Update this query to handle idempotency
    final var sql =
        """
        INSERT INTO reservation(id, room_id, reserved_from, reserved_to)
        VALUES(:reservationId, :roomId, :reservedFrom, :reservedTo)
        """
            .trim();

    UUID reservationId = UUID.fromString(reservation.id().value());
    UUID roomId = UUID.fromString(reservation.roomId());
    Date reservedFrom = Date.valueOf(reservation.reservationDateRange().from());
    Date reservedTo = Date.valueOf(reservation.reservationDateRange().to());

    jdbcClient
        .sql(sql)
        .param("reservationId", reservationId)
        .param("roomId", roomId)
        .param("reservedFrom", reservedFrom)
        .param("reservedTo", reservedTo)
        .update();
  }
}
