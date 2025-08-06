package org.egualpam.contexts.hotel.customer.hotel.application.query;

public record FindHotelsQuery(String location, Integer minPrice, Integer maxPrice) {}
