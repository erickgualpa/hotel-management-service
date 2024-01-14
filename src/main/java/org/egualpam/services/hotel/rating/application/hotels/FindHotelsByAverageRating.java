package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.hotels.Location;
import org.egualpam.services.hotel.rating.domain.hotels.Price;
import org.egualpam.services.hotel.rating.domain.hotels.exception.PriceRangeValuesSwapped;

import java.util.List;
import java.util.Optional;
import java.util.function.ToDoubleFunction;

import static java.util.Comparator.comparingDouble;

public final class FindHotelsByAverageRating {

    private static final ToDoubleFunction<Hotel> getHotelAverageRating = h -> h.getAverageRating().value();

    private final HotelRepository hotelRepository;

    public FindHotelsByAverageRating(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<HotelDto> execute(Filters filters) {
        Optional<Location> location = Optional.ofNullable(filters.location())
                .map(Location::new);

        Optional<Price> minPrice = Optional.ofNullable(filters.priceBegin())
                .map(Price::new);

        Optional<Price> maxPrice = Optional.ofNullable(filters.priceEnd())
                .map(Price::new);

        maxPrice.ifPresent(
                max -> minPrice.filter(min -> min.value() < max.value())
                        .orElseThrow(PriceRangeValuesSwapped::new)
        );

        return hotelRepository.find(location, minPrice, maxPrice)
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