package org.egualpam.services.hotelmanagement.application.hotels;

import org.egualpam.services.hotelmanagement.application.shared.InternalQuery;
import org.egualpam.services.hotelmanagement.application.shared.ViewSupplier;
import org.egualpam.services.hotelmanagement.domain.hotels.HotelCriteria;
import org.egualpam.services.hotelmanagement.domain.hotels.Location;
import org.egualpam.services.hotelmanagement.domain.hotels.Price;
import org.egualpam.services.hotelmanagement.domain.hotels.PriceRange;

import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparingDouble;

public class FindHotels implements InternalQuery<HotelsView> {

    private final Optional<Location> location;
    private final PriceRange priceRange;
    private final ViewSupplier<HotelsView> hotelsViewSupplier;

    public FindHotels(
            Optional<String> location,
            Optional<Integer> minPrice,
            Optional<Integer> maxPrice,
            ViewSupplier<HotelsView> hotelsViewSupplier
    ) {
        this.location = location.map(Location::new);
        this.priceRange = new PriceRange(
                minPrice.map(Price::new),
                maxPrice.map(Price::new)
        );
        this.hotelsViewSupplier = hotelsViewSupplier;
    }

    @Override
    public HotelsView get() {
        List<HotelsView.Hotel> hotels =
                hotelsViewSupplier.get(new HotelCriteria(location, priceRange))
                        .hotels().stream()
                        .sorted(comparingDouble(HotelsView.Hotel::averageRating).reversed())
                        .toList();
        return new HotelsView(hotels);
    }
}
