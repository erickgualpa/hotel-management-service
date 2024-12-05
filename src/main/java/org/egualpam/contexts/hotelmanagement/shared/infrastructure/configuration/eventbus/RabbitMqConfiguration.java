package org.egualpam.contexts.hotelmanagement.shared.infrastructure.configuration.eventbus;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Bean
  public Channel channel(RabbitMqProperties rabbitMqProperties) {
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
    final Channel channel;
    try {
      // TODO: Consider using a connection pool
      connection = factory.newConnection();

      channel = connection.createChannel();
      channel.queueDeclare("hotelmanagement.hotel", false, false, false, null);
      channel.queueDeclare("hotelmanagement.review", false, false, false, null);
      // TODO: Workaround for having a dlq but should be updated too
      channel.queueDeclare("hotelmanagement.dlq", false, false, false, null);
    } catch (IOException | TimeoutException e) {
      throw new RuntimeException("Connection could not be established", e);
    }

    return channel;
  }
}
