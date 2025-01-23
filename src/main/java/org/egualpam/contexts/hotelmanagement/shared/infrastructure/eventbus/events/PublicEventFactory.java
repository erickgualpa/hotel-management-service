package org.egualpam.contexts.hotelmanagement.shared.infrastructure.eventbus.events;

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
import java.util.Set;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCreated;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelRatingUpdated;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCreated;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewUpdated;
import org.egualpam.contexts.hotelmanagement.shared.domain.DomainEvent;

public final class PublicEventFactory {

  private PublicEventFactory() {}

  public static PublicEvent from(DomainEvent domainEvent) {
    return switch (domainEvent) {
      case ReviewCreated reviewCreated ->
          new ReviewCreatedPublicEvent(
              reviewCreated.id().value(),
              reviewCreated.aggregateId().value(),
              reviewCreated.occurredOn(),
              reviewCreated.hotelId().value(),
              reviewCreated.rating().value());
      case ReviewUpdated reviewUpdated ->
          new ReviewUpdatedPublicEvent(
              reviewUpdated.id().value(),
              reviewUpdated.aggregateId().value(),
              reviewUpdated.occurredOn());
      case HotelCreated hotelCreated ->
          new HotelCreatedPublicEvent(
              hotelCreated.id().value(),
              hotelCreated.aggregateId().value(),
              hotelCreated.occurredOn());
      case HotelRatingUpdated hotelRatingUpdated ->
          new HotelRatingUpdatedPublicEvent(
              hotelRatingUpdated.id().value(),
              hotelRatingUpdated.aggregateId().value(),
              hotelRatingUpdated.reviewId().value(),
              hotelRatingUpdated.occurredOn());
      default -> throw new UnsupportedDomainEvent(domainEvent);
    };
  }

  public static PublicEvent mapAndValidate(
      DomainEvent domainEvent, ObjectMapper objectMapper, JsonSchemaFactory jsonSchemaFactory) {
    return switch (domainEvent) {
      case ReviewCreated reviewCreated ->
          new ReviewCreatedPublicEvent(
              reviewCreated.id().value(),
              reviewCreated.aggregateId().value(),
              reviewCreated.occurredOn(),
              reviewCreated.hotelId().value(),
              reviewCreated.rating().value());
      case ReviewUpdated reviewUpdated ->
          new ReviewUpdatedPublicEvent(
              reviewUpdated.id().value(),
              reviewUpdated.aggregateId().value(),
              reviewUpdated.occurredOn());
      case HotelCreated hotelCreated -> {
        HotelCreatedPublicEvent publicEvent =
            new HotelCreatedPublicEvent(
                hotelCreated.id().value(),
                hotelCreated.aggregateId().value(),
                hotelCreated.occurredOn());

        // TODO: All this validation code should be cleaned up
        final JsonSchema schema =
            loadSchema(jsonSchemaFactory, "events/hotel/hotel-created/1-0.json");

        JsonNode publicEventAsJsonNode = objectMapper.valueToTree(publicEvent);
        Set<ValidationMessage> result = schema.validate(publicEventAsJsonNode);

        if (!result.isEmpty()) {
          // TODO: Log result content
          throw new UnsupportedDomainEvent(hotelCreated);
        }

        yield publicEvent;
      }
      case HotelRatingUpdated hotelRatingUpdated ->
          new HotelRatingUpdatedPublicEvent(
              hotelRatingUpdated.id().value(),
              hotelRatingUpdated.aggregateId().value(),
              hotelRatingUpdated.reviewId().value(),
              hotelRatingUpdated.occurredOn());
      default -> throw new UnsupportedDomainEvent(domainEvent);
    };
  }

  private static JsonSchema loadSchema(
      JsonSchemaFactory jsonSchemaFactory, String schemaAsResourcePath) {
    URL schemaAsResource = DomainEvent.class.getClassLoader().getResource(schemaAsResourcePath);
    try {
      requireNonNull(schemaAsResource);
      final File schemaAsFile = new File(schemaAsResource.toURI());
      final String schemaAsString = Files.readString(schemaAsFile.toPath(), StandardCharsets.UTF_8);
      return jsonSchemaFactory.getSchema(schemaAsString);
    } catch (Exception e) {
      throw new RuntimeException("Schema can not be loaded", e);
    }
  }
}
