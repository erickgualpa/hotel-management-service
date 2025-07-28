package org.egualpam.contexts.hotelmanagement.room.infrastructure.configuration;

import org.egualpam.contexts.hotelmanagement.room.application.command.CreateRoom;
import org.egualpam.contexts.hotelmanagement.room.domain.Room;
import org.egualpam.contexts.hotelmanagement.room.infrastructure.cqrs.command.simple.SyncCreateRoomCommand;
import org.egualpam.contexts.hotelmanagement.room.infrastructure.cqrs.command.simple.SyncCreateRoomCommandHandler;
import org.egualpam.contexts.hotelmanagement.room.infrastructure.repository.JdbcRoomRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class RoomInfrastructureConfiguration {

  @Bean
  public AggregateRepository<Room> roomRepository(JdbcClient jdbcClient) {
    return new JdbcRoomRepository(jdbcClient);
  }

  @Bean
  public SimpleCommandBusConfiguration roomSimpleCommandBusConfiguration(
      TransactionTemplate transactionTemplate, CreateRoom createRoom) {
    return new SimpleCommandBusConfiguration()
        .handling(
            SyncCreateRoomCommand.class,
            new SyncCreateRoomCommandHandler(transactionTemplate, createRoom));
  }
}
