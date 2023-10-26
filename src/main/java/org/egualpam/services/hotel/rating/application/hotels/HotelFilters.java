package org.egualpam.services.hotel.rating.application.hotels;

public enum HotelFilters {
    LOCATION("location"),
    PRICE_RANGE_BEGIN("priceRangeBegin"),
    PRICE_RANGE_END("priceRangeEnd");

    private final String value;

    HotelFilters(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
