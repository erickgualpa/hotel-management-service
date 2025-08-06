package org.egualpam.contexts.hotel.shared.infrastructure.configuration;

import java.util.UUID;
import java.util.function.Supplier;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("org.egualpam.contexts.hotel.shared.infrastructure.jpa")
public class SharedInfrastructureConfiguration {

  @Bean
  public Supplier<UniqueId> uniqueIdSupplierV2() {
    return () -> new UniqueId(UUID.randomUUID().toString());
  }
}
