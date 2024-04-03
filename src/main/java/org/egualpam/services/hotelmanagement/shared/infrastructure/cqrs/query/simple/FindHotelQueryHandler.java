package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.hotels.application.query.SingleHotelView;
import org.egualpam.services.hotelmanagement.hotels.domain.HotelCriteria;
import org.egualpam.services.hotelmanagement.shared.application.query.Query;
import org.egualpam.services.hotelmanagement.shared.application.query.View;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;

@RequiredArgsConstructor
public class FindHotelQueryHandler implements QueryHandler {

    private final ViewSupplier<SingleHotelView> singleHotelViewSupplier;

    @Override
    public View handle(Query query) {
        final FindHotelQuery findHotelQuery = (FindHotelQuery) query;
        return singleHotelViewSupplier.get(
                new HotelCriteria(findHotelQuery.getHotelId())
        );
    }
}
