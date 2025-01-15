package org.egualpam.contexts.hotelmanagement.shared.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.configuration.eventbus.RabbitMqProperties;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.EventStoreTestRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.HotelTestRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.RabbitMqTestConsumer;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.helpers.ReviewTestRepository;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class SharedTestConfiguration {

  @Bean
  public HotelTestRepository hotelTestRepository(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    return new HotelTestRepository(namedParameterJdbcTemplate);
  }

  @Bean
  public ReviewTestRepository reviewTestRepository(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    return new ReviewTestRepository(namedParameterJdbcTemplate);
  }

  @Bean
  public EventStoreTestRepository eventStoreTestRepository(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    return new EventStoreTestRepository(namedParameterJdbcTemplate);
  }

  @Bean
  public RabbitMqTestConsumer rabbitMqConsumerForTest(
      RabbitMqProperties rabbitMqProperties, ObjectMapper objectMapper)
      throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(rabbitMqProperties.getHost());
    factory.setPort(rabbitMqProperties.getAmqpPort());
    factory.setUsername(rabbitMqProperties.getAdminUsername());
    factory.setPassword(rabbitMqProperties.getAdminPassword());

    Connection connection = factory.newConnection();
    return new RabbitMqTestConsumer(connection, objectMapper);
  }

  @Bean("hotelManagementTestQueue")
  public Queue hotelManagementTestQueue() {
    return new Queue("hotelmanagement.test", false);
  }

  @Bean("hotelManagementTestBinding")
  public Binding hotelManagementTestBinding(
      @Qualifier("hotelManagementTestQueue") Queue queue, TopicExchange topicExchange) {
    return BindingBuilder.bind(queue).to(topicExchange).with("hotelmanagement.#");
  }
}
