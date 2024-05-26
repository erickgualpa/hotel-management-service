package org.egualpam.services.hotelmanagement.shared.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.shared.application.command.CommandBus;
import org.egualpam.services.hotelmanagement.shared.application.query.QueryBus;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.SimpleQueryBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.SimpleQueryBusConfiguration;
import org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.rabbitmq.RabbitMqPublicEventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.simple.SimplePublicEventBus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Configuration
public class SharedConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info().title("Hotel Management Service API")
                );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    // TODO: Rename this bean into 'simplePublicEventBus'
    @Bean
    public PublicEventBus simplePublicEventBus(EntityManager entityManager) {
        return new SimplePublicEventBus(entityManager);
    }

    @Primary
    @Bean
    public PublicEventBus rabbitMqPublicEventBus(
            @Value("${message-broker.rabbitmq.host}") String rabbitMqHost,
            @Value("${message-broker.rabbitmq.amqp-port}") int rabbitMqAmqpPort,
            @Value("${message-broker.rabbitmq.admin-username}") String rabbitMqAdminUsername,
            @Value("${message-broker.rabbitmq.admin-password}") String rabbitMqAdminPassword
    ) {
        return new RabbitMqPublicEventBus(
                rabbitMqHost,
                rabbitMqAmqpPort,
                rabbitMqAdminUsername,
                rabbitMqAdminPassword
        );
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
