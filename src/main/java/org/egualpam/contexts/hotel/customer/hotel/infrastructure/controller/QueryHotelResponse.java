package org.egualpam.contexts.hotel.customer.hotel.infrastructure.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;

@JsonSerialize
public record QueryHotelResponse(List<Hotel> hotels) {
  record Hotel(
      String identifier,
      String name,
      String description,
      String location,
      Integer price,
      String imageURL,
      @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = "#.##") Double averageRating) {}
}
