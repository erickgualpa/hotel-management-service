package org.egualpam.services.hotelmanagement.hotels.infrastructure.cqrs.query.simple;

import org.egualpam.services.hotelmanagement.hotels.application.query.MultipleHotelsView;
import org.egualpam.services.hotelmanagement.hotels.domain.exception.PriceRangeValuesSwapped;
import org.egualpam.services.hotelmanagement.shared.application.query.QueryBus;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.FindHotelsQuery;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.FindHotelsQueryHandler;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.SimpleQueryBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class SimpleQueryBusShould {

    @Mock
    ViewSupplier<MultipleHotelsView> multipleHotelsViewSupplier;

    private QueryBus testee;

    @BeforeEach
    void setUp() {
        testee = new SimpleQueryBus(
                Map.of(FindHotelsQuery.class, new FindHotelsQueryHandler(multipleHotelsViewSupplier))
        );
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

        assertThrows(PriceRangeValuesSwapped.class, () -> testee.publish(query));
    }
}
