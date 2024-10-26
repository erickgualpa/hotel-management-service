package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record GetHotelResponse(Hotel hotel) {
  record Hotel(
      String identifier,
      String name,
      String description,
      String location,
      Integer price,
      String imageURL,
      @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = "#.##") Double averageRating) {}
}
