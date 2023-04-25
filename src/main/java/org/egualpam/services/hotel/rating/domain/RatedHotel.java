package org.egualpam.services.hotel.rating.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RatedHotel {

    private String identifier;
    private String name;
    private String description;
    private HotelLocation location;
    private Integer totalPrice;
    private String imageURL;

    public RatedHotel() {}

    public RatedHotel(
            String identifier,
            String name,
            String description,
            HotelLocation location,
            Integer totalPrice,
            String imageURL) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.location = location;
        this.totalPrice = totalPrice;
        this.imageURL = imageURL;
    }
}
