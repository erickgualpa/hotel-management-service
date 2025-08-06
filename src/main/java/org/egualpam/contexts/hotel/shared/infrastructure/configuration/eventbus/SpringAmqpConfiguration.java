package org.egualpam.contexts.hotel.shared.infrastructure.configuration.eventbus;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAmqpConfiguration {

  static final String DOMAIN_EVENTS_EXCHANGE = "hotelmanagement.domain-events";

  @Bean
  public TopicExchange topicExchange() {
    return new TopicExchange(DOMAIN_EVENTS_EXCHANGE);
  }
}
