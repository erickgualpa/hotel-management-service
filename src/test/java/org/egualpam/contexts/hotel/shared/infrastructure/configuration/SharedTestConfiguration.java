package org.egualpam.contexts.hotel.shared.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.egualpam.contexts.hotel.management.room.application.command.CreateRoom;
import org.egualpam.contexts.hotel.shared.infrastructure.configuration.eventbus.RabbitMqProperties;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.EventStoreTestRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.HotelRatingTestRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.HotelTestRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.RabbitMqTestConsumer;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.ReservationTestRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.ReviewTestRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.RoomPriceTestRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.helpers.RoomTestRepository;
import org.egualpam.contexts.hotelmanagement.reservation.application.command.CreateReservation;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class SharedTestConfiguration {

  @Bean
  public HotelTestRepository hotelTestRepository(
      ObjectMapper objectMapper, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    return new HotelTestRepository(objectMapper, namedParameterJdbcTemplate);
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
  public HotelRatingTestRepository hotelRatingTestRepository(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    return new HotelRatingTestRepository(namedParameterJdbcTemplate);
  }

  @Bean
  public RoomTestRepository roomTestRepository(JdbcClient jdbcClient, CreateRoom createRoom) {
    return new RoomTestRepository(jdbcClient, createRoom);
  }

  @Bean
  public ReservationTestRepository reservationTestRepository(
      JdbcClient jdbcClient, CreateReservation createReservation) {
    return new ReservationTestRepository(jdbcClient, createReservation);
  }

  @Bean
  public RoomPriceTestRepository roomPriceTestRepository(JdbcClient jdbcClient) {
    return new RoomPriceTestRepository(jdbcClient);
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
