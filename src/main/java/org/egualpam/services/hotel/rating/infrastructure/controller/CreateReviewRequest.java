package org.egualpam.services.hotel.rating.infrastructure.controller;

public record CreateReviewRequest(String hotelIdentifier, Integer rating, String comment) {
}
