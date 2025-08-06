package org.egualpam.contexts.hotel.management.hotel.infrastructure.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record PostHotelRequest(
    String id, String name, String description, String location, Integer price, String imageURL) {}
