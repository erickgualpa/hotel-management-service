package org.egualpam.contexts.hotelmanagement.review.application.command;

public record CreateReviewCommand(
    String reviewIdentifier, String hotelIdentifier, Integer rating, String comment) {}
