package org.egualpam.services.hotel.rating.application;

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

        public Builder withPriceRange(Integer min, Integer max) {
            query.priceRange = new PriceRange(min, max);
            return this;
        }

        public HotelQuery build() {
            return query;
        }
    }
}
