package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.application.shared.InternalQuery;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelCriteria;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.egualpam.services.hotel.rating.domain.hotels.PriceRange;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;
import org.egualpam.services.hotel.rating.domain.shared.Criteria;

import java.util.List;
import java.util.Optional;
import java.util.function.ToDoubleFunction;

import static java.util.Comparator.comparingDouble;

public class FindHotels implements InternalQuery<HotelsView> {

    private static final ToDoubleFunction<Hotel> getHotelAverageRating = h -> h.getAverageRating().value();

    private final Optional<Location> location;
    private final PriceRange priceRange;
    private final AggregateRepository<Hotel> aggregateHotelRepository;

    public FindHotels(
            Optional<String> location,
            Optional<Integer> minPrice,
            Optional<Integer> maxPrice,
            AggregateRepository<Hotel> aggregateHotelRepository
    ) {
        this.location = location.map(Location::new);
        this.priceRange = new PriceRange(
                minPrice.map(Price::new),
                maxPrice.map(Price::new)
        );
        this.aggregateHotelRepository = aggregateHotelRepository;
    }

    @Override
    public HotelsView get() {
        Criteria criteria = new HotelCriteria(
                location,
                priceRange
        );
        List<HotelsView.Hotel> hotels = aggregateHotelRepository.find(criteria)
                .stream()
                .sorted(comparingDouble(getHotelAverageRating).reversed())
                .map(this::mapIntoView)
                .toList();
        return new HotelsView(hotels);
    }

    private HotelsView.Hotel mapIntoView(Hotel hotel) {
        return new HotelsView.Hotel(
                hotel.getId().value().toString(),
                hotel.getName().value(),
                hotel.getDescription().value(),
                hotel.getLocation().value(),
                hotel.getTotalPrice().value(),
                hotel.getImageURL().value(),
                hotel.getAverageRating().value()
        );
    }
}