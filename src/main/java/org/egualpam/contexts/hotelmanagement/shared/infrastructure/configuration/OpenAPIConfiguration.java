package org.egualpam.contexts.hotelmanagement.shared.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI().info(new Info().title("Hotel Management Service API"));
  }
}
