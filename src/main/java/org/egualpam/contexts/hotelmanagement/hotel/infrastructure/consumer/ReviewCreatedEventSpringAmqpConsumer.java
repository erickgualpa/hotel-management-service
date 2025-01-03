package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.consumer;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.time.Instant;
import org.slf4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class ReviewCreatedEventSpringAmqpConsumer {

  private final Logger logger = getLogger(this.getClass());

  private final ObjectMapper objectMapper;

  public ReviewCreatedEventSpringAmqpConsumer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @RabbitListener(queues = "hotelmanagement.review", ackMode = "MANUAL")
  public void consume(Message in, Channel channel) throws IOException {
    DomainEvent domainEvent = objectMapper.readValue(in.getBody(), DomainEvent.class);

    if (!"hotelmanagement.review.created".equals(domainEvent.type())) {
      channel.basicNack(in.getMessageProperties().getDeliveryTag(), false, true);
    }

    logger.info("Domain event consumed: [%s]".formatted(domainEvent));
    channel.basicAck(in.getMessageProperties().getDeliveryTag(), true);
  }

  record DomainEvent(
      String id, String type, String version, String aggregateId, Instant occurredOn) {}
}
