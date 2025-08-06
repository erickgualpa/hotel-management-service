package org.egualpam.contexts.hotel.customer.review.application.command;

public record CreateReviewCommand(
    String reviewIdentifier, String hotelIdentifier, Integer rating, String comment) {}
