package org.egualpam.contexts.hotel.shared.infrastructure.configuration.eventbus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import org.egualpam.contexts.hotel.shared.domain.EventBus;
import org.egualpam.contexts.hotel.shared.infrastructure.eventbus.shared.EventStoreRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.eventbus.simple.SimpleEventBus;
import org.egualpam.contexts.hotel.shared.infrastructure.eventbus.springamqp.SpringAmqpEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@EnableConfigurationProperties(RabbitMqProperties.class)
public class EventBusConfiguration {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Bean
  public EventStoreRepository eventStoreRepository(
      ObjectMapper objectMapper, NamedParameterJdbcTemplate jdbcTemplate) {
    return new EventStoreRepository(objectMapper, jdbcTemplate);
  }

  @Bean
  public EventBus simpleEventBus(
      ObjectMapper objectMapper,
      JsonSchemaFactory jsonSchemaFactory,
      NamedParameterJdbcTemplate jdbcTemplate) {
    return new SimpleEventBus(objectMapper, jsonSchemaFactory, jdbcTemplate);
  }

  @Primary
  @Bean
  public EventBus springAmqpEventBus(
      ObjectMapper objectMapper,
      JsonSchemaFactory jsonSchemaFactory,
      EventStoreRepository eventStoreRepository,
      RabbitTemplate rabbitTemplate) {
    rabbitTemplate.setExchange(SpringAmqpConfiguration.DOMAIN_EVENTS_EXCHANGE);
    return new SpringAmqpEventBus(
        objectMapper, jsonSchemaFactory, eventStoreRepository, rabbitTemplate);
  }
}
