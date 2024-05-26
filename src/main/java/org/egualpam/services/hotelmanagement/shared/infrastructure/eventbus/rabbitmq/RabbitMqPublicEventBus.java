package org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.transaction.Transactional;
import org.egualpam.services.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class RabbitMqPublicEventBus implements PublicEventBus {

    private final String rabbitMqHost;
    private final int rabbitMqAmqpPort;
    private final String rabbitMqAdminUsername;
    private final String rabbitMqAdminPassword;

    public RabbitMqPublicEventBus(
            String rabbitMqHost,
            int rabbitMqAmqpPort,
            String rabbitMqAdminUsername,
            String rabbitMqAdminPassword
    ) {
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
            channel.basicPublish("", "hotelmanagement.reviews", null, "Hello, World!".getBytes());
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
