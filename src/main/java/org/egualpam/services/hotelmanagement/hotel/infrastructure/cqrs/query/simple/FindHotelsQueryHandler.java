package org.egualpam.services.hotelmanagement.hotel.infrastructure.cqrs.query.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.hotel.application.query.FindHotelsQuery;
import org.egualpam.services.hotelmanagement.hotel.application.query.MultipleHotelsView;
import org.egualpam.services.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.services.hotelmanagement.shared.application.query.Query;
import org.egualpam.services.hotelmanagement.shared.application.query.View;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;

@RequiredArgsConstructor
public class FindHotelsQueryHandler implements QueryHandler {

    private final ViewSupplier<MultipleHotelsView> multipleHotelsViewSupplier;

    @Override
    public View handle(Query query) {
        FindHotelsQuery findHotelsQuery = (FindHotelsQuery) query;
        return multipleHotelsViewSupplier.get(
                new HotelCriteria(
                        findHotelsQuery.getLocation().orElse(null),
                        findHotelsQuery.getMinPrice().orElse(null),
                        findHotelsQuery.getMaxPrice().orElse(null)
                )
        );
    }
}