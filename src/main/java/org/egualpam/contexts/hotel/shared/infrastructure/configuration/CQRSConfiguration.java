package org.egualpam.contexts.hotel.shared.infrastructure.configuration;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.CommandBus;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.simple.SimpleCommandBus;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.QueryBus;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.simple.SimpleQueryBus;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.simple.SimpleQueryBusConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CQRSConfiguration {

  @Bean
  public CommandBus commandBus(List<SimpleCommandBusConfiguration> configurations) {
    return new SimpleCommandBus(
        configurations.stream()
            .map(SimpleCommandBusConfiguration::handlers)
            .flatMap(m -> m.entrySet().stream())
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }

  @Bean
  public QueryBus queryBus(List<SimpleQueryBusConfiguration> configurations) {
    return new SimpleQueryBus(
        configurations.stream()
            .map(SimpleQueryBusConfiguration::handlers)
            .flatMap(m -> m.entrySet().stream())
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }
}
