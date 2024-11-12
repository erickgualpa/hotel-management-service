package org.egualpam.contexts.hotelmanagement.hotel.application.query;

public record FindHotelsQuery(String location, Integer minPrice, Integer maxPrice) {}
