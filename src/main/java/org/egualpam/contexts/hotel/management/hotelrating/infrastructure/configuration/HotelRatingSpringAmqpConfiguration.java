package org.egualpam.contexts.hotel.management.hotelrating.infrastructure.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelRatingSpringAmqpConfiguration {

  @Bean("initializeHotelRatingQueue")
  public Queue initializeHotelRatingQueue() {
    return new Queue("hotelmanagement.hotel-rating.initialize-hotel-rating", false);
  }

  @Bean("initializeHotelRatingBinding")
  public Binding initializeHotelRatingBinding(
      @Qualifier("initializeHotelRatingQueue") Queue queue, TopicExchange topicExchange) {
    return BindingBuilder.bind(queue).to(topicExchange).with("hotelmanagement.hotel.created");
  }

  @Bean("updateHotelRatingQueueV2")
  public Queue updateHotelRatingQueue() {
    return new Queue("hotelmanagement.hotel-rating.update-hotel-rating", false);
  }

  @Bean("updateHotelRatingBindingV2")
  public Binding updateHotelRatingBinding(
      @Qualifier("updateHotelRatingQueueV2") Queue queue, TopicExchange topicExchange) {
    return BindingBuilder.bind(queue).to(topicExchange).with("hotelmanagement.review.created");
  }
}
