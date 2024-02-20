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

public class FindHotels implements InternalQuery<List<HotelDto>> {

    private static final ToDoubleFunction<Hotel> getHotelAverageRating = h -> h.getAverageRating().value();

    private final Optional<String> locationFilter;
    private final Optional<Integer> minPriceFilter;
    private final Optional<Integer> maxPriceFilter;
    private final AggregateRepository<Hotel> aggregateHotelRepository;

    public FindHotels(
            Optional<String> locationFilter,
            Optional<Integer> minPriceFilter,
            Optional<Integer> maxPriceFilter,
            AggregateRepository<Hotel> aggregateHotelRepository
    ) {
        this.locationFilter = locationFilter;
        this.minPriceFilter = minPriceFilter;
        this.maxPriceFilter = maxPriceFilter;
        this.aggregateHotelRepository = aggregateHotelRepository;
    }

    @Override
    public List<HotelDto> get() {
        Optional<Location> location = locationFilter.map(Location::new);
        PriceRange priceRange = new PriceRange(
                minPriceFilter.map(Price::new),
                maxPriceFilter.map(Price::new)
        );
        Criteria criteria = new HotelCriteria(
                location,
                priceRange
        );
        return aggregateHotelRepository.find(criteria)
                .stream()
                .sorted(comparingDouble(getHotelAverageRating).reversed())
                .map(this::mapIntoHotelDto)
                .toList();
    }

    private HotelDto mapIntoHotelDto(Hotel hotel) {
        return new HotelDto(
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
