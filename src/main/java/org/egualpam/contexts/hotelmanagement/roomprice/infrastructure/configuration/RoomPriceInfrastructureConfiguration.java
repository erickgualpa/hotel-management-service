package org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.configuration;

import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.roomprice.application.command.UpdateRoomPrice;
import org.egualpam.contexts.hotelmanagement.roomprice.domain.RoomPrice;
import org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.cqrs.command.SyncUpdateRoomPriceCommandHandler;
import org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.cqrs.command.simple.SyncUpdateRoomPriceCommand;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class RoomPriceInfrastructureConfiguration {

  @Bean
  public AggregateRepository<RoomPrice> roomPriceAggregateRepository() {
    return new AggregateRepository<RoomPrice>() {
      @Override
      public Optional<RoomPrice> find(AggregateId id) {
        throw new RuntimeException("Not yet implemented!");
      }

      @Override
      public void save(RoomPrice aggregate) {}
    };
  }

  @Bean
  public SimpleCommandBusConfiguration roomPriceSimpleCommandBusConfiguration(
      TransactionTemplate transactionTemplate, UpdateRoomPrice updateRoomPrice) {
    return new SimpleCommandBusConfiguration()
        .handling(
            SyncUpdateRoomPriceCommand.class,
            new SyncUpdateRoomPriceCommandHandler(transactionTemplate, updateRoomPrice));
  }
}
