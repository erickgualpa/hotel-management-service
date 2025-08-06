package org.egualpam.contexts.hotel.management.hotelrating.infrastructure.repository.jpa;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Set;

@Converter
class PersistenceReviewsConverter implements AttributeConverter<PersistenceReviews, String> {

  private final ObjectMapper objectMapper;

  public PersistenceReviewsConverter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public String convertToDatabaseColumn(PersistenceReviews persistenceReviews) {
    if (isNull(persistenceReviews)) {
      return null;
    }

    try {
      return objectMapper.writeValueAsString(persistenceReviews);
    } catch (JsonProcessingException e) {
      // TODO: Replace by custom exception if needed
      throw new RuntimeException("Reviews could not be converted to String", e);
    }
  }

  @Override
  public PersistenceReviews convertToEntityAttribute(String value) {
    if (isNull(value)) {
      return new PersistenceReviews(Set.of());
    }

    try {
      return objectMapper.readValue(value, PersistenceReviews.class);
    } catch (JsonProcessingException e) {
      // TODO: Replace by custom exception if needed
      throw new RuntimeException("Reviews could not be converted to persistence entity", e);
    }
  }
}
