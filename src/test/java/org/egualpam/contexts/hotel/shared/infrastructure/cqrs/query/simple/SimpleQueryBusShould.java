package org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.simple;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.Query;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.QueryBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleQueryBusShould {

  private QueryBus testee;

  @BeforeEach
  void setUp() {
    testee = new SimpleQueryBus(Collections.emptyMap());
  }

  @Test
  void throwException_whenCriteriaNotMatchesAnyHandler() {
    Query query = new Query() {};
    assertThrows(QueryHandlerNotFound.class, () -> testee.publish(query));
  }
}
