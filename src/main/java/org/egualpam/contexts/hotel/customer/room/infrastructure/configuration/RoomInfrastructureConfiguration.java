package org.egualpam.contexts.hotel.customer.room.infrastructure.configuration;

import org.egualpam.contexts.hotel.customer.room.application.query.FindRooms;
import org.egualpam.contexts.hotel.customer.room.application.query.ManyRooms;
import org.egualpam.contexts.hotel.customer.room.domain.RoomCriteria;
import org.egualpam.contexts.hotel.customer.room.infrastructure.cqrs.query.simple.SyncFindRoomsHandler;
import org.egualpam.contexts.hotel.customer.room.infrastructure.cqrs.query.simple.SyncFindRoomsQuery;
import org.egualpam.contexts.hotel.customer.room.infrastructure.readmodelsupplier.JdbcManyRoomsReadModelSupplier;
import org.egualpam.contexts.hotel.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.simple.SimpleQueryBusConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration("customerRoomInfrastructureConfiguration")
public class RoomInfrastructureConfiguration {

  @Bean
  public ReadModelSupplier<RoomCriteria, ManyRooms> manyRoomsReadModelSupplier(
      JdbcClient jdbcClient) {
    return new JdbcManyRoomsReadModelSupplier(jdbcClient);
  }

  @Bean
  public SimpleQueryBusConfiguration roomSimpleQueryBusConfiguration(FindRooms findRooms) {
    return new SimpleQueryBusConfiguration()
        .handling(SyncFindRoomsQuery.class, new SyncFindRoomsHandler(findRooms));
  }
}
