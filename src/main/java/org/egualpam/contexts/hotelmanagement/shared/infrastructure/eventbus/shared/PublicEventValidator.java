package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.HotelCreatedPublicEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events.PublicEvent;

public final class PublicEventValidator {

  private final ObjectMapper objectMapper;
  private final JsonSchemaFactory jsonSchemaFactory;

  private final Map<Class<? extends PublicEvent>, String> jsonSchemas =
      Map.of(HotelCreatedPublicEvent.class, "events/hotel/hotel-created/1-0.json");

  public PublicEventValidator(ObjectMapper objectMapper, JsonSchemaFactory jsonSchemaFactory) {
    this.objectMapper = objectMapper;
    this.jsonSchemaFactory = jsonSchemaFactory;
  }

  public void validate(PublicEvent event) {
    // TODO: Check if this actions can be performed on application startup
    String jsonSchemaPath = jsonSchemas.get(event.getClass());

    if (jsonSchemaPath == null) {
      return;
    }

    JsonSchema jsonSchema = loadSchema(jsonSchemaPath);
    JsonNode eventAsJsonNode = objectMapper.valueToTree(event);

    Set<ValidationMessage> result = jsonSchema.validate(eventAsJsonNode);

    if (!result.isEmpty()) {
      // TODO: Use custom exception
      throw new RuntimeException(
          "Event [%s] is not valid due to: [%s]".formatted(event, result.toString()));
    }
  }

  private JsonSchema loadSchema(String schemaAsResourcePath) {
    URL schemaAsResource = DomainEvent.class.getClassLoader().getResource(schemaAsResourcePath);
    try {
      requireNonNull(schemaAsResource);
      final File schemaAsFile = new File(schemaAsResource.toURI());
      final String schemaAsString = Files.readString(schemaAsFile.toPath(), StandardCharsets.UTF_8);
      return this.jsonSchemaFactory.getSchema(schemaAsString);
    } catch (Exception e) {
      throw new RuntimeException("Schema can not be loaded", e);
    }
  }
}
