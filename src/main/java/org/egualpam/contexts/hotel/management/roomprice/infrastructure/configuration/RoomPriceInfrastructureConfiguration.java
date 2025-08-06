package org.egualpam.contexts.hotel.management.roomprice.infrastructure.configuration;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.nameUUIDFromBytes;

import java.util.Optional;
import org.egualpam.contexts.hotel.management.roomprice.application.command.UpdateRoomPrice;
import org.egualpam.contexts.hotel.management.roomprice.domain.RoomPrice;
import org.egualpam.contexts.hotel.management.roomprice.domain.RoomPriceIdGenerator;
import org.egualpam.contexts.hotel.management.roomprice.infrastructure.cqrs.command.SyncUpdateRoomPriceCommandHandler;
import org.egualpam.contexts.hotel.management.roomprice.infrastructure.cqrs.command.simple.SyncUpdateRoomPriceCommand;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class RoomPriceInfrastructureConfiguration {

  @Bean
  public RoomPriceIdGenerator roomPriceIdGenerator() {
    return ((hotelId, roomType) -> {
      final var id = hotelId.value() + "_" + roomType.name();
      final var value = nameUUIDFromBytes(id.getBytes(UTF_8)).toString();
      return new AggregateId(value);
    });
  }

  @Bean
  public AggregateRepository<RoomPrice> roomPriceAggregateRepository() {
    return new AggregateRepository<>() {
      @Override
      public Optional<RoomPrice> find(AggregateId id) {
        return Optional.empty();
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
