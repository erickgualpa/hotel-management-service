package org.egualpam.contexts.hotel.management.hotelrating.application.command;

public record UpdateHotelRatingCommand(String hotelId, String reviewId, Integer reviewRating) {}
