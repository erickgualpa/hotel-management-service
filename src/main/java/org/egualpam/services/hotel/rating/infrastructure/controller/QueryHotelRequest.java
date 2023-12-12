package org.egualpam.services.hotel.rating.infrastructure.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record QueryHotelRequest(String location, QueryHotelRequest.PriceRange priceRange) {
    record PriceRange(Integer begin, Integer end) {
    }
}
