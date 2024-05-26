package org.egualpam.services.hotelmanagement.shared.infrastructure;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.egualpam.services.hotelmanagement.shared.infrastructure.configuration.SharedTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;

@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@SpringBootTest(
        classes = HotelManagementServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ContextConfiguration(
        initializers = AbstractIntegrationTest.PostgreSqlInitializer.class,
        classes = {SharedTestConfiguration.class}
)
public abstract class AbstractIntegrationTest {

    protected static final WireMockServer wireMockServer = new WireMockServer(8081);

    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest");

    // TODO: Make this private
    protected static final RabbitMQContainer rabbitMqContainer =
            // TODO: Check if the version is the latest
            new RabbitMQContainer("rabbitmq:3.7.25-management-alpine")
                    .withExposedPorts(5672, 15672);

    static {
        wireMockServer.start();
        postgreSQLContainer.start();
        rabbitMqContainer.start();
    }

    @Autowired
    protected MockMvc mockMvc;

    static class PostgreSqlInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    // PostgreSQL properties
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username= " + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.datasource.driver-class-name=" + postgreSQLContainer.getDriverClassName(),

                    // RabbitMQ properties
                    "message-broker.rabbitmq.host=" + rabbitMqContainer.getHost(),
                    "message-broker.rabbitmq.amqp-port= " + rabbitMqContainer.getAmqpPort(),
                    "message-broker.rabbitmq.admin-username=" + rabbitMqContainer.getAdminUsername(),
                    "message-broker.rabbitmq.admin-password=" + rabbitMqContainer.getAdminPassword()
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}
