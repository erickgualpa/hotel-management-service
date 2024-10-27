package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotelsQuery;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotelmanagement.hotel.domain.PriceRangeValuesSwapped;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindHotelsQueryHandlerShould {

  @Mock ReadModelSupplier<ManyHotels> readModelSupplier;

  private QueryHandler testee;

  @BeforeEach
  void setUp() {
    testee = new FindHotelsQueryHandler(readModelSupplier);
  }

  @Test
  void throwDomainException_whenHotelCriteriaHasPriceRangeValuesSwapped() {
    Integer minPrice = 100;
    Integer maxPrice = 50;

    FindHotelsQuery query = new FindHotelsQuery(null, minPrice, maxPrice);

    assertThrows(PriceRangeValuesSwapped.class, () -> testee.handle(query));
  }
}
