package org.egualpam.contexts.hotelmanagement.shared.infrastructure.configuration;

import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueIdSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SharedInfrastructureConfiguration {
  @Bean
  public UniqueIdSupplier uniqueIdSupplier() {
    return UniqueId::get;
  }
}
