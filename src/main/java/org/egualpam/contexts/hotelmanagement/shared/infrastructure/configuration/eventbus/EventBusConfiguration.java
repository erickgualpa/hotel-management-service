package org.egualpam.contexts.hotelmanagement.shared.infrastructure.configuration.eventbus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.rabbitmq.RabbitMqEventBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.simple.SimpleEventBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.springamqp.SpringAmqpEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(RabbitMqProperties.class)
public class EventBusConfiguration {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Bean
  public EventBus simpleEventBus(EntityManager entityManager) {
    return new SimpleEventBus(entityManager);
  }

  @Bean
  public EventBus rabbitMqEventBus(ObjectMapper objectMapper, Channel channel) {
    return new RabbitMqEventBus(channel, objectMapper);
  }

  @Primary
  @Bean
  public EventBus springAmqpEventBus(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
    rabbitTemplate.setExchange(SpringAmqpConfiguration.DOMAIN_EVENTS_EXCHANGE);
    return new SpringAmqpEventBus(objectMapper, rabbitTemplate);
  }
}
