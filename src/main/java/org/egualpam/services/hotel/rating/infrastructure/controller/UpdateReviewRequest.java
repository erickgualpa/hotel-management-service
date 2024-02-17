package org.egualpam.services.hotel.rating.infrastructure.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record UpdateReviewRequest(String comment) {
}
