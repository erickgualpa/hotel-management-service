package org.egualpam.services.hotelmanagement.hotels.infrastructure.cqrs.query.simple;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotelmanagement.hotels.application.query.MultipleHotelsView;
import org.egualpam.services.hotelmanagement.hotels.domain.HotelCriteria;
import org.egualpam.services.hotelmanagement.shared.application.query.View;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;

@RequiredArgsConstructor
public class FindHotelsQueryHandler implements QueryHandler<FindHotelsQuery> {

    private final ViewSupplier<MultipleHotelsView> multipleHotelsViewSupplier;

    @Override
    public View handle(FindHotelsQuery query) {
        return multipleHotelsViewSupplier.get(
                new HotelCriteria(
                        query.getLocation().orElse(null),
                        query.getMinPrice().orElse(null),
                        query.getMaxPrice().orElse(null)
                )
        );
    }
}