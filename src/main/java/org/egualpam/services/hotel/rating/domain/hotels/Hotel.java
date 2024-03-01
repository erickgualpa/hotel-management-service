package org.egualpam.services.hotel.rating.domain.hotels;

import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRoot;
import org.egualpam.services.hotel.rating.domain.shared.DomainEvent;

import java.util.ArrayList;
import java.util.List;

public final class Hotel implements AggregateRoot {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private final AggregateId id;
    private final HotelName name;
    private final HotelDescription description;
    private final Location location;
    private final Price totalPrice;
    private final ImageURL imageURL;
    private final AverageRating averageRating;

    public Hotel(
            AggregateId id,
            HotelName name,
            HotelDescription description,
            Location location,
            Price totalPrice,
            ImageURL imageURL,
            AverageRating averageRating
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.totalPrice = totalPrice;
        this.imageURL = imageURL;
        this.averageRating = averageRating;
    }

    @Override
    public AggregateId getId() {
        return id;
    }

    @Override
    public List<DomainEvent> pullDomainEvents() {
        return this.domainEvents;
    }

    public HotelName getName() {
        return name;
    }

    public HotelDescription getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    public Price getTotalPrice() {
        return totalPrice;
    }

    public ImageURL getImageURL() {
        return imageURL;
    }

    public AverageRating getAverageRating() {
        return averageRating;
    }
}
