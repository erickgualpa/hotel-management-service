package org.egualpam.services.hotel.rating.application;

import java.time.LocalDate;

// TODO: Check which fields on this query make sense to keep
public final class HotelQuery {

    private String location;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private PriceRange priceRange;

    public String getLocation() {
        return location;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public static class PriceRange {

        private final Integer begin;
        private final Integer end;

        public PriceRange(Integer begin, Integer end) {
            this.begin = begin;
            this.end = end;
        }

        public Integer getBegin() {
            return begin;
        }

        public Integer getEnd() {
            return end;
        }
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

        public Builder withCheckIn(LocalDate checkIn) {
            query.checkIn = checkIn;
            return this;
        }

        public Builder withCheckOut(LocalDate checkOut) {
            query.checkOut = checkOut;
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
