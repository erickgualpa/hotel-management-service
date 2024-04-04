package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import org.egualpam.services.hotelmanagement.shared.application.query.Query;
import org.egualpam.services.hotelmanagement.shared.application.query.QueryBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleQueryBusShould {

    private QueryBus testee;

    @BeforeEach
    void setUp() {
        testee = new SimpleQueryBus(Collections.emptyMap());
    }

    @Test
    void throwException_whenCriteriaNotMatchesAnyHandler() {
        Query query = new Query() {
        };
        assertThrows(QueryHandlerNotFound.class, () -> testee.publish(query));
    }
}