package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.application.shared.InternalQuery;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.egualpam.services.hotel.rating.domain.hotels.PriceRange;

import java.util.List;
import java.util.Optional;
import java.util.function.ToDoubleFunction;

import static java.util.Comparator.comparingDouble;

public class FindHotels implements InternalQuery<List<HotelDto>> {

    private static final ToDoubleFunction<Hotel> getHotelAverageRating = h -> h.getAverageRating().value();

    private final Optional<String> locationFilter;
    private final Optional<Integer> minPriceFilter;
    private final Optional<Integer> maxPriceFilter;
    private final HotelRepository hotelRepository;

    public FindHotels(
            Optional<String> locationFilter,
            Optional<Integer> minPriceFilter,
            Optional<Integer> maxPriceFilter,
            HotelRepository hotelRepository) {
        this.locationFilter = locationFilter;
        this.minPriceFilter = minPriceFilter;
        this.maxPriceFilter = maxPriceFilter;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<HotelDto> get() {
        Optional<Location> location = locationFilter.map(Location::new);
        PriceRange priceRange = new PriceRange(
                minPriceFilter.map(Price::new),
                maxPriceFilter.map(Price::new)
        );

        return hotelRepository.find(location, priceRange)
                .stream()
                .sorted(comparingDouble(getHotelAverageRating).reversed())
                .map(this::mapIntoHotelDto)
                .toList();
    }

    private HotelDto mapIntoHotelDto(Hotel hotel) {
        return new HotelDto(
                hotel.getIdentifier().value(),
                hotel.getName().value(),
                hotel.getDescription().value(),
                hotel.getLocation().value(),
                hotel.getTotalPrice().value(),
                hotel.getImageURL().value(),
                hotel.getAverageRating().value()
        );
    }
}
