package org.egualpam.services.hotel.rating.domain.hotels;

import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRoot;
import org.egualpam.services.hotel.rating.domain.shared.DomainEvent;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Hotel implements AggregateRoot {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private final AggregateId aggregateId;
    private final Identifier identifier;
    private final HotelName name;
    private final HotelDescription description;
    private final Location location;
    private final Price totalPrice;
    private final ImageURL imageURL;
    private final AverageRating averageRating;

    public Hotel(
            Identifier identifier,
            HotelName name,
            HotelDescription description,
            Location location,
            Price totalPrice,
            ImageURL imageURL,
            AverageRating averageRating
    ) {
        this.aggregateId = new AggregateId(UUID.fromString(identifier.value()));
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.location = location;
        this.totalPrice = totalPrice;
        this.imageURL = imageURL;
        this.averageRating = averageRating;
    }

    @Override
    public AggregateId getId() {
        return aggregateId;
    }

    @Override
    public List<DomainEvent> getDomainEvents() {
        return this.domainEvents;
    }

    public Identifier getIdentifier() {
        return identifier;
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
