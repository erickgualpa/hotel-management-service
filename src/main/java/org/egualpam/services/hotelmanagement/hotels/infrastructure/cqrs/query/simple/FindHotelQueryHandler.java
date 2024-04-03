package org.egualpam.services.hotelmanagement.hotels.infrastructure.cqrs.query.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.hotels.application.query.SingleHotelView;
import org.egualpam.services.hotelmanagement.hotels.domain.HotelCriteria;
import org.egualpam.services.hotelmanagement.shared.application.query.View;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;

@RequiredArgsConstructor
public class FindHotelQueryHandler implements QueryHandler<FindHotelQuery> {

    private final ViewSupplier<SingleHotelView> singleHotelViewSupplier;

    @Override
    public View handle(FindHotelQuery query) {
        return singleHotelViewSupplier.get(
                new HotelCriteria(query.getHotelId())
        );
    }
}
