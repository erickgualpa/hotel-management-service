package org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.services.hotelmanagement.shared.domain.EventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.events.PublicEvent;
import org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.events.PublicEventFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

// TODO: Refactor this whole class
public final class RabbitMqEventBus implements EventBus {

    private final Logger logger = LoggerFactory.getLogger(RabbitMqEventBus.class);

    private final Connection connection;
    private final ObjectMapper objectMapper;

    public RabbitMqEventBus(Connection connection, ObjectMapper objectMapper) {
        this.connection = connection;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(List<DomainEvent> events) {
        List<PublicEvent> publicEvents = events.stream().map(PublicEventFactory::from).toList();
        try (Channel channel = connection.createChannel()) {
            // TODO: Queues should be already configured
            channel.queueDeclare("hotelmanagement.reviews", false, false, false, null);
            publicEvents.forEach(e -> publishEvent(e, channel));
        } catch (IOException | TimeoutException e) {
            // TODO: Consider using a custom exception
            throw new RuntimeException(e);
        }
    }

    private void publishEvent(PublicEvent publicEvent, Channel channel) {
        try {
            byte[] serializedEvent = objectMapper.writeValueAsBytes(publicEvent);
            channel.basicPublish("", "hotelmanagement.reviews", null, serializedEvent);
            logger.info("Event {} has been published", publicEvent.getType());
        } catch (JsonProcessingException ex) {
            // TODO: Consider using a custom exception
            throw new RuntimeException("Domain event could not be serialized", ex);
        } catch (IOException ex) {
            throw new RuntimeException("Domain event could not be published", ex);
        }
    }
}
