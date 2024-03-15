package org.egualpam.services.hotel.rating.application.reviews;

import org.egualpam.services.hotel.rating.application.hotels.HotelView;
import org.egualpam.services.hotel.rating.application.shared.InternalQuery;
import org.egualpam.services.hotel.rating.application.shared.ViewSupplier;
import org.egualpam.services.hotel.rating.domain.hotels.HotelCriteria;
import org.egualpam.services.hotel.rating.domain.shared.AggregateId;

public final class FindHotel implements InternalQuery<HotelView> {

    private final AggregateId hotelId;
    private final ViewSupplier<HotelView> hotelViewSupplier;

    public FindHotel(String hotelId, ViewSupplier<HotelView> hotelViewSupplier) {
        this.hotelId = new AggregateId(hotelId);
        this.hotelViewSupplier = hotelViewSupplier;
    }

    @Override
    public HotelView get() {
        return hotelViewSupplier.get(
                new HotelCriteria(hotelId)
        );
    }
}
