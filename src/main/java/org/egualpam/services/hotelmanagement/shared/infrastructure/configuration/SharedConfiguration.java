package org.egualpam.services.hotelmanagement.shared.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.shared.application.command.CommandBus;
import org.egualpam.services.hotelmanagement.shared.application.query.QueryBus;
import org.egualpam.services.hotelmanagement.shared.domain.EventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.configuration.properties.eventbus.RabbitMqProperties;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.SimpleQueryBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.SimpleQueryBusConfiguration;
import org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.rabbitmq.RabbitMqEventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.simple.SimpleEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.util.stream.Collectors.toMap;

@EnableConfigurationProperties(RabbitMqProperties.class)
@Configuration
public class SharedConfiguration {

    private final Logger logger = LoggerFactory.getLogger(SharedConfiguration.class);

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info().title("Hotel Management Service API")
                );
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public EventBus simpleEventBus(EntityManager entityManager) {
        return new SimpleEventBus(entityManager);
    }

    @Primary
    @Bean
    public EventBus rabbitMqEventBus(RabbitMqProperties rabbitMqProperties, ObjectMapper objectMapper) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMqProperties.getHost());
        factory.setPort(rabbitMqProperties.getAmqpPort());
        factory.setUsername(rabbitMqProperties.getAdminUsername());
        factory.setPassword(rabbitMqProperties.getAdminPassword());

        logger.info("Connecting to RabbitMQ at {}:{}", rabbitMqProperties.getHost(), rabbitMqProperties.getAmqpPort());

        final Connection connection;
        try {
            // TODO: Consider using a connection pool
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException("Connection could not be established", e);
        }

        return new RabbitMqEventBus(connection, objectMapper);
    }

    @Bean
    public CommandBus commandBus(List<SimpleCommandBusConfiguration> configurations) {
        return new SimpleCommandBus(
                configurations.stream()
                        .map(SimpleCommandBusConfiguration::getHandlers)
                        .flatMap(m -> m.entrySet().stream())
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    @Bean
    public QueryBus queryBus(List<SimpleQueryBusConfiguration> configurations) {
        return new SimpleQueryBus(
                configurations.stream()
                        .map(SimpleQueryBusConfiguration::getHandlers)
                        .flatMap(m -> m.entrySet().stream())
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }
}
