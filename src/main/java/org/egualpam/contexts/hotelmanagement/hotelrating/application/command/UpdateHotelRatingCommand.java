package org.egualpam.contexts.hotelmanagement.hotelrating.application.command;

public record UpdateHotelRatingCommand(String hotelId, String reviewId, Integer reviewRating) {}
