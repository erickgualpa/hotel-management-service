package org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class RabbitMqPublicEventBus implements PublicEventBus {

    private final ObjectMapper objectMapper;
    private final String rabbitMqHost;
    private final int rabbitMqAmqpPort;
    private final String rabbitMqAdminUsername;
    private final String rabbitMqAdminPassword;

    public RabbitMqPublicEventBus(
            ObjectMapper objectMapper,
            String rabbitMqHost,
            int rabbitMqAmqpPort,
            String rabbitMqAdminUsername,
            String rabbitMqAdminPassword
    ) {
        this.objectMapper = objectMapper;
        this.rabbitMqHost = rabbitMqHost;
        this.rabbitMqAmqpPort = rabbitMqAmqpPort;
        this.rabbitMqAdminUsername = rabbitMqAdminUsername;
        this.rabbitMqAdminPassword = rabbitMqAdminPassword;
    }

    @Transactional
    @Override
    public void publish(List<DomainEvent> events) {
        // TODO: Implement RabbitMQ publish logic
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMqHost);
        factory.setPort(rabbitMqAmqpPort);
        factory.setUsername(rabbitMqAdminUsername);
        factory.setPassword(rabbitMqAdminPassword);

        Connection connection;
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        // TODO: Consider using a connection pool
        try (Channel channel = connection.createChannel()) {
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
