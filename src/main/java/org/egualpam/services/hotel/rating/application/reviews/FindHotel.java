package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.hotels.HotelView;
import org.egualpam.services.hotel.rating.application.shared.InternalQuery;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.shared.AggregateId;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;

public final class FindHotel implements InternalQuery<HotelView> {

    private final AggregateId hotelId;
    private final AggregateRepository<Hotel> aggregateHotelRepository;

    public FindHotel(String hotelId, AggregateRepository<Hotel> aggregateHotelRepository) {
        this.hotelId = new AggregateId(hotelId);
        this.aggregateHotelRepository = aggregateHotelRepository;
    }

    @Override
    public HotelView get() {
        Hotel hotel = aggregateHotelRepository.find(hotelId);
        return new HotelView(
                new HotelView.Hotel(
                        hotel.getId().value().toString(),
                        hotel.getName().value(),
                        hotel.getDescription().value(),
                        hotel.getLocation().value(),
                        hotel.getTotalPrice().value(),
                        hotel.getImageURL().value(),
                        hotel.getAverageRating().value()
                )
        );
    }
}
