package org.egualpam.contexts.hotelmanagement.shared.infrastructure.configuration.eventbus;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAmqpConfiguration {

  static final String DOMAIN_EVENTS_EXCHANGE = "hotelmanagement.domain-events";

  @Bean
  public TopicExchange topicExchange() {
    return new TopicExchange(DOMAIN_EVENTS_EXCHANGE);
  }

  @Bean("hotelQueue")
  public Queue hotelQueue() {
    return new Queue("hotelmanagement.hotel", false);
  }

  @Bean("hotelBinding")
  public Binding hotelBinding(@Qualifier("hotelQueue") Queue queue, TopicExchange topicExchange) {
    return BindingBuilder.bind(queue).to(topicExchange).with("hotelmanagement.hotel.#");
  }

  @Bean("reviewQueue")
  public Queue reviewQueue() {
    return new Queue("hotelmanagement.review", false);
  }

  @Bean("reviewBinding")
  public Binding reviewBinding(@Qualifier("reviewQueue") Queue queue, TopicExchange topicExchange) {
    return BindingBuilder.bind(queue).to(topicExchange).with("hotelmanagement.review.#");
  }
}
