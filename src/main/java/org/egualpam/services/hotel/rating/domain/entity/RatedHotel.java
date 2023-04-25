package org.egualpam.services.hotel.rating.domain.entity;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RatedHotel {

    private final String identifier;

    public RatedHotel() {
        this.identifier = UUID.randomUUID().toString();
    }
}
