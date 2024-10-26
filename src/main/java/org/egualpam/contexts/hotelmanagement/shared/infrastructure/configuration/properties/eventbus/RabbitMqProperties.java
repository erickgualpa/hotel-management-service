package org.egualpam.contexts.hotelmanagement.shared.infrastructure.configuration.properties.eventbus;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "message-broker.rabbitmq")
public class RabbitMqProperties {

  private final String host;
  private final int amqpPort;
  private final String adminUsername;
  private final String adminPassword;

  public RabbitMqProperties(String host, int amqpPort, String adminUsername, String adminPassword) {
    this.host = host;
    this.amqpPort = amqpPort;
    this.adminUsername = adminUsername;
    this.adminPassword = adminPassword;
  }

  public String getHost() {
    return host;
  }

  public int getAmqpPort() {
    return amqpPort;
  }

  public String getAdminUsername() {
    return adminUsername;
  }

  public String getAdminPassword() {
    return adminPassword;
  }
}
