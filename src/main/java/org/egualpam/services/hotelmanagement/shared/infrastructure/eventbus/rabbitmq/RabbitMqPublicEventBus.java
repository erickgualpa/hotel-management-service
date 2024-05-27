package org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.configuration.properties.eventbus.RabbitMqProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeoutException;

// TODO: Refactor this whole class
public class RabbitMqPublicEventBus implements PublicEventBus {

    private final Logger logger = LoggerFactory.getLogger(RabbitMqPublicEventBus.class);

    private final Connection rabbitMqConnection;
    private final ObjectMapper objectMapper;

    public RabbitMqPublicEventBus(
            RabbitMqProperties rabbitMqProperties,
            ObjectMapper objectMapper
    ) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMqProperties.getHost());
        factory.setPort(rabbitMqProperties.getAmqpPort());
        factory.setUsername(rabbitMqProperties.getAdminUsername());
        factory.setPassword(rabbitMqProperties.getAdminPassword());

        logger.info("Connecting to RabbitMQ at {}:{}", rabbitMqProperties.getHost(), rabbitMqProperties.getAmqpPort());

        try {
            // TODO: Consider using a connection pool
            this.rabbitMqConnection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException("Connection could not be established", e);
        }

        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(List<DomainEvent> events) {
        try (Channel channel = rabbitMqConnection.createChannel()) {
            // TODO: Queues should be already configured
            channel.queueDeclare("hotelmanagement.reviews", false, false, false, null);
            events.forEach(e -> publishEvent(e, channel));
        } catch (IOException | TimeoutException e) {
            // TODO: Consider using a custom exception
            throw new RuntimeException(e);
        }
    }

    private void publishEvent(DomainEvent domainEvent, Channel channel) {
        try {
            PublicEvent publicEvent = new PublicEvent(
                    domainEvent.getId().toString(),
                    domainEvent.getType(),
                    domainEvent.getAggregateId().value(),
                    // TODO: Check if this is the correct way to convert Instant to OffsetDateTime
                    domainEvent.getOccurredOn()
            );
            byte[] serializedEvent = objectMapper.writeValueAsBytes(publicEvent);
            channel.basicPublish("", "hotelmanagement.reviews", null, serializedEvent);
            logger.info("Event {} has been published", domainEvent.getType());
        } catch (JsonProcessingException ex) {
            // TODO: Consider using a custom exception
            throw new RuntimeException("Domain event could not be serialized", ex);
        } catch (IOException ex) {
            throw new RuntimeException("Domain event could not be published", ex);
        }
    }

    @JsonSerialize
    record PublicEvent(
            String id,
            String type,
            String aggregateId,
            Instant occurredOn
    ) {
    }
}
