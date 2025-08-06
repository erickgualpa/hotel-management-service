package org.egualpam.contexts.hotel.shared.infrastructure.configuration;

import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonSchemaConfiguration {

  @Bean
  public JsonSchemaFactory jsonSchemaFactory() {
    return JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
  }
}
