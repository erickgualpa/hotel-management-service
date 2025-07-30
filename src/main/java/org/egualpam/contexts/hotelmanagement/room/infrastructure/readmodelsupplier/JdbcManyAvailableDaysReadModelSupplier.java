package org.egualpam.contexts.hotelmanagement.room.infrastructure.readmodelsupplier;

import java.util.List;
import org.egualpam.contexts.hotelmanagement.room.application.query.ManyAvailableDays;
import org.egualpam.contexts.hotelmanagement.room.domain.RoomCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.springframework.jdbc.core.simple.JdbcClient;

public class JdbcManyAvailableDaysReadModelSupplier
    implements ReadModelSupplier<RoomCriteria, ManyAvailableDays> {

  private final JdbcClient jdbcClient;

  public JdbcManyAvailableDaysReadModelSupplier(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public ManyAvailableDays get(RoomCriteria criteria) {
    return new ManyAvailableDays(List.of(new ManyAvailableDays.AvailableDay(1, 8, 2025)));
  }
}
