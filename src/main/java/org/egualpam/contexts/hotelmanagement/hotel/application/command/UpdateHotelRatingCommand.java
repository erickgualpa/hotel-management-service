package org.egualpam.contexts.hotelmanagement.hotel.application.command;

public record UpdateHotelRatingCommand(String hotelId, String reviewId, Integer reviewRating) {}
