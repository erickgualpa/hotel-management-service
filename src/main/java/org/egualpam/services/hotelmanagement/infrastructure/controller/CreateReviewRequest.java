package org.egualpam.services.hotelmanagement.infrastructure.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record CreateReviewRequest(String hotelId, Integer rating, String comment) {
}
