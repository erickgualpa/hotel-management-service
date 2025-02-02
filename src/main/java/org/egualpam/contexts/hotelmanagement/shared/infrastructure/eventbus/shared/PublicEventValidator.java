package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared;

import static java.util.Map.entry;
import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared.events.HotelCreatedPublicEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared.events.HotelRatingInitializedPublicEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared.events.HotelRatingUpdatePublicEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared.events.PublicEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared.events.ReviewCreatedPublicEvent;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.shared.events.ReviewUpdatedPublicEvent;

public final class PublicEventValidator {

  private final ObjectMapper objectMapper;
  private final JsonSchemaFactory jsonSchemaFactory;

  private final Map<Class<? extends PublicEvent>, String> jsonSchemas =
      Map.ofEntries(
          entry(HotelCreatedPublicEvent.class, "events/schema/hotel/created/1-0.json"),
          entry(ReviewCreatedPublicEvent.class, "events/schema/review/created/1-0.json"),
          entry(ReviewUpdatedPublicEvent.class, "events/schema/review/updated/1-0.json"),
          entry(
              HotelRatingInitializedPublicEvent.class,
              "events/schema/hotel-rating/initialized/1-0.json"),
          entry(HotelRatingUpdatePublicEvent.class, "events/schema/hotel-rating/updated/1-0.json"));

  public PublicEventValidator(ObjectMapper objectMapper, JsonSchemaFactory jsonSchemaFactory) {
    this.objectMapper = objectMapper;
    this.jsonSchemaFactory = jsonSchemaFactory;
  }

  public void validate(PublicEvent event) {
    // TODO: Check if this actions can be performed on application startup
    String jsonSchemaPath = jsonSchemas.get(event.getClass());

    if (jsonSchemaPath == null) {
      throw new MissingPublicEventSchema(event);
    }

    JsonSchema jsonSchema = loadSchema(jsonSchemaPath);
    JsonNode eventAsJsonNode = objectMapper.valueToTree(event);

    Set<ValidationMessage> result = jsonSchema.validate(eventAsJsonNode);

    if (!result.isEmpty()) {
      throw new InvalidPublicEvent(event, result);
    }
  }

  private JsonSchema loadSchema(String schemaAsResourcePath) {
    try (InputStream schemaAsInputStream =
        DomainEvent.class.getClassLoader().getResourceAsStream(schemaAsResourcePath)) {
      requireNonNull(schemaAsInputStream);

      String schemaAsString =
          new String(schemaAsInputStream.readAllBytes(), StandardCharsets.UTF_8);

      return this.jsonSchemaFactory.getSchema(schemaAsString);
    } catch (Exception e) {
      throw new RuntimeException("Schema can not be loaded", e);
    }
  }
}
