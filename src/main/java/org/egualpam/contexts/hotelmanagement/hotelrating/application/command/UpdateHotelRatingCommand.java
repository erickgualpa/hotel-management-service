package org.egualpam.contexts.hotelmanagement.hotelrating.application.command;

public record UpdateHotelRatingCommand(String id, String reviewId, Integer reviewRating) {}
