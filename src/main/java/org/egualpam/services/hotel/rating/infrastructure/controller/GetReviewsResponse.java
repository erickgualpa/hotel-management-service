package org.egualpam.services.hotel.rating.infrastructure.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
public record GetReviewsResponse(List<Review> reviews) {
    record Review(
            Integer rating,
            String comment) {
    }
}
