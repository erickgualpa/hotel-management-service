package org.egualpam.contexts.hotelmanagement.shared.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.rabbitmq.RabbitMqEventBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.simple.SimpleEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class EventBusConfiguration {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Bean
  public EventBus simpleEventBus(EntityManager entityManager) {
    return new SimpleEventBus(entityManager);
  }

  @Primary
  @Bean
  public EventBus rabbitMqEventBus(
      RabbitMqProperties rabbitMqProperties, ObjectMapper objectMapper) {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(rabbitMqProperties.getHost());
    factory.setPort(rabbitMqProperties.getAmqpPort());
    factory.setUsername(rabbitMqProperties.getAdminUsername());
    factory.setPassword(rabbitMqProperties.getAdminPassword());

    logger.info(
        "Connecting to RabbitMQ at {}:{}",
        rabbitMqProperties.getHost(),
        rabbitMqProperties.getAmqpPort());

    final Connection connection;
    try {
      // TODO: Consider using a connection pool
      connection = factory.newConnection();
    } catch (IOException | TimeoutException e) {
      throw new RuntimeException("Connection could not be established", e);
    }

    return new RabbitMqEventBus(connection, objectMapper);
  }
}
