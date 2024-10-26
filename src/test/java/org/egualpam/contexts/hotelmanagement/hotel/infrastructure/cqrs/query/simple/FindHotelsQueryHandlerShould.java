package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple;

import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotelsQuery;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.MultipleHotelsView;
import org.egualpam.contexts.hotelmanagement.hotel.domain.exceptions.PriceRangeValuesSwapped;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class FindHotelsQueryHandlerShould {

    @Mock
    ViewSupplier<MultipleHotelsView> multipleHotelsViewSupplier;

    private QueryHandler testee;

    @BeforeEach
    void setUp() {
        testee = new FindHotelsQueryHandler(multipleHotelsViewSupplier);
    }

    @Test
    void throwDomainException_whenHotelCriteriaHasPriceRangeValuesSwapped() {
        Integer minPrice = 100;
        Integer maxPrice = 50;

        FindHotelsQuery query = new FindHotelsQuery(
                null,
                minPrice,
                maxPrice
        );

        assertThrows(PriceRangeValuesSwapped.class, () -> testee.handle(query));
    }
}
