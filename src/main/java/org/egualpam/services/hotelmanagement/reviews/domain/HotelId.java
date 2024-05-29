package org.egualpam.services.hotelmanagement.reviews.domain;

import org.egualpam.services.hotelmanagement.shared.domain.UniqueId;

import java.util.Objects;

public final class HotelId {

    private final UniqueId value;

    public HotelId(String value) {
        this.value = new UniqueId(value);
    }

    public String value() {
        return value.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelId hotelId = (HotelId) o;
        return Objects.equals(value, hotelId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
