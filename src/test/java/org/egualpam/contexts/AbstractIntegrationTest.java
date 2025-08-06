package org.egualpam.contexts;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.egualpam.contexts.hotel.shared.infrastructure.configuration.SharedTestConfiguration;
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
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(
    initializers = {
      AbstractIntegrationTest.PostgreSqlInitializer.class,
      AbstractIntegrationTest.RabbitMqInitializer.class
    },
    classes = {SharedTestConfiguration.class})
public abstract class AbstractIntegrationTest {

  // TODO: Amend location os this class and the SpringBootApplication one

  protected static final WireMockServer wireMockServer = new WireMockServer(8081);

  private static final PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>("postgres:latest");

  private static final RabbitMQContainer rabbitMqContainer =
      new RabbitMQContainer("rabbitmq:3.13.2-management-alpine");

  static {
    wireMockServer.start();
    postgreSQLContainer.start();
    rabbitMqContainer.start();
  }

  @Autowired protected MockMvc mockMvc;

  static class PostgreSqlInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      TestPropertyValues.of(
              "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
              "spring.datasource.username= " + postgreSQLContainer.getUsername(),
              "spring.datasource.password=" + postgreSQLContainer.getPassword(),
              "spring.datasource.driver-class-name=" + postgreSQLContainer.getDriverClassName())
          .applyTo(applicationContext.getEnvironment());
    }
  }

  static class RabbitMqInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      TestPropertyValues.of(
              "message-broker.rabbitmq.host=" + rabbitMqContainer.getHost(),
              "message-broker.rabbitmq.amqp-port=" + rabbitMqContainer.getAmqpPort(),
              "message-broker.rabbitmq.admin-username=" + rabbitMqContainer.getAdminUsername(),
              "message-broker.rabbitmq.admin-password=" + rabbitMqContainer.getAdminPassword())
          .applyTo(applicationContext.getEnvironment());
    }
  }
}
