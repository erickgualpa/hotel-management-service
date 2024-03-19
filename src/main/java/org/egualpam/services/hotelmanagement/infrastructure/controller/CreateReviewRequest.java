package org.egualpam.services.hotelmanagement.infrastructure.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record CreateReviewRequest(String hotelIdentifier, Integer rating, String comment) {
}