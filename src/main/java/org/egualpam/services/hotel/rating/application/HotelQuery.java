package org.egualpam.services.hotel.rating.application;

import static java.util.Objects.nonNull;

public final class HotelQuery {

    private String location;
    private PriceRange priceRange;

    public String getLocation() {
        return location;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public record PriceRange(Integer begin, Integer end) {
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        private final HotelQuery query;

        private Builder() {
            this.query = new HotelQuery();
        }

        public Builder withLocation(String location) {
            query.location = location;
            return this;
        }

        public Builder withPriceRange(Integer begin, Integer end) {
            if ((nonNull(begin) && nonNull(end))
                    && end < begin) {
                query.priceRange = new PriceRange(end, begin);
            } else {
                query.priceRange = new PriceRange(begin, end);
            }
            return this;
        }

        public HotelQuery build() {
            return query;
        }
    }
}
