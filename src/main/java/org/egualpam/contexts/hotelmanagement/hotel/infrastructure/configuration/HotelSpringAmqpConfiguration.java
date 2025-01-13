package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelSpringAmqpConfiguration {

  @Bean("updateHotelRatingQueue")
  public Queue updateHotelRatingQueue() {
    return new Queue("hotelmanagement.hotel.update-hotel-rating", false);
  }

  @Bean("updateHotelRatingBinding")
  public Binding updateHotelRatingBinding(
      @Qualifier("updateHotelRatingQueue") Queue queue, TopicExchange topicExchange) {
    return BindingBuilder.bind(queue).to(topicExchange).with("hotelmanagement.review.created");
  }
}
