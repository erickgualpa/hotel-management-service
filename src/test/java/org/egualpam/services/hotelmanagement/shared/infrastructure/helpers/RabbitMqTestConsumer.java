package org.egualpam.services.hotelmanagement.shared.infrastructure.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.GetResponse;
import org.egualpam.services.hotelmanagement.e2e.models.PublicEventResult;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class RabbitMqTestConsumer {

    private final Connection connection;
    private final ObjectMapper objectMapper;

    public RabbitMqTestConsumer(Connection connection, ObjectMapper objectMapper) {
        this.connection = connection;
        this.objectMapper = objectMapper;
    }

    public PublicEventResult consumeFromQueue(String queueName) throws IOException {
        Channel channel;
        try {
            channel = this.connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
        } catch (IOException e) {
            throw new RuntimeException("Channel could be created", e);
        }

        boolean autoAck = true;
        GetResponse response;
        try {
            response = channel.basicGet(queueName, autoAck);
        } catch (IOException e) {
            throw new RuntimeException("Queue could not be consumed", e);
        }

        try {
            channel.close();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException("Connection could not be closed", e);
        }

        assertNotNull(response);
        return objectMapper.readValue(response.getBody(), PublicEventResult.class);
    }
}
